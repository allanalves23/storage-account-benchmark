package com.testdiskperf.demo.shared;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.io.ByteArrayInputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class FileReaderService {

    private final ObjectMapper mapper;

    @Value("${storage.account.connectionString}")
    private String storageAccountConnectionString;

    @Value("${storage.account.containerSmall}")
    private String containerSmall;

    public FileReaderService(ObjectMapper mapper) {
        this.mapper = mapper;
        this.mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    private List<DataMockDTO> readFile(Path baseFile) throws IOException {
        byte[] jsonBytes = Files.readAllBytes(baseFile);
        List<DataMockDTO> items = mapper.readValue(jsonBytes, new TypeReference<List<DataMockDTO>>() {});
        return items;
    }

    private void validateOutputDirectory(Path outputDir) throws IOException {
        if (Files.notExists(outputDir)) {
            Files.createDirectories(outputDir);
        }
    }

    private String getFileName(DataMockDTO data) {
        return setTimeStamp("ID: "+ data.id(), data);
    }

    private String getFileName(DataMockDTO data, int index) {
        return setTimeStamp("ID: "+ (index+1), data); 
    }

    private String setTimeStamp(String content, DataMockDTO data) {
        return content + " - TimeStamp: " + Instant.now().getEpochSecond() + " - " + Instant.now().toEpochMilli() + ".json";
    }

    public void saveFilesInDisk(Path baseFile, Path outputDir) throws IOException {
        validateOutputDirectory(outputDir);
        List<DataMockDTO> items = readFile(baseFile);

        for (int i = 0; i < items.size(); i++) {
            DataMockDTO p = items.get(i);
            String filename =  getFileName(p, i);
            try (FileOutputStream fos = new FileOutputStream(outputDir.resolve(filename).toString())) {
                FileDescriptor fd = fos.getFD();
                String value = mapper.writeValueAsString(p);
                fos.write(value.getBytes(StandardCharsets.UTF_8));
                fos.flush();
                fd.sync();
            } catch (IOException e) {
                System.err.println("Error during synchronous file write: " + e.getMessage());
                throw e;
            }
        }
    }

    public void saveFilesInStorageAccount(Path baseFile) throws IOException {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            .connectionString(storageAccountConnectionString)
            .buildClient();

        List<DataMockDTO> items = readFile(baseFile);
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(this.containerSmall);
        for (int i = 0; i < items.size(); i++) {
            BlobClient blobClient = containerClient.getBlobClient(getFileName(items.get(i), i));
            byte[] jsonBytes = mapper.writeValueAsBytes(items.get(i));
            blobClient.upload(new ByteArrayInputStream(jsonBytes));
        }
    }
}