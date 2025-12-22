package com.testdiskperf.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testdiskperf.demo.shared.FileReaderService;
import java.nio.file.Path;
import java.io.PrintWriter;
import java.io.StringWriter;


@RestController
public class TestConnection {
    @Value("${small-data.base.path}")
    private String basePath;

    @Value("${small-data.out.path}")
    private String outDir;

    private final FileReaderService service;

    public TestConnection(FileReaderService service) {
        this.service = service;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        try {
            service.saveFilesInDisk(Path.of(this.basePath), Path.of(this.outDir));
            return ResponseEntity.ok("Arquivos criados com sucesso");
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));

            return ResponseEntity.status(500).body("Erro: " + e.getMessage() + "\n\n\n\n" + sw.toString() + "\n\n\n\n" + Path.of(".").toAbsolutePath());
        }
    }

    @GetMapping("/test-storage-account")
    public ResponseEntity<String> testStorageAccount() {
        try {
            service.saveFilesInStorageAccount(Path.of(this.basePath));
            return ResponseEntity.ok("Arquivos criados no storage account com sucesso");
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));

            return ResponseEntity.status(500).body("Erro: " + e.getMessage() + "\n\n\n\n" + sw.toString() + "\n\n\n\n" + Path.of(".").toAbsolutePath());
        }
    }

}