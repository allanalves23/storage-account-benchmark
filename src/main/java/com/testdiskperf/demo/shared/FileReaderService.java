package com.testdiskperf.demo.shared;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.stereotype.Service;
import java.time.Instant;
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
}