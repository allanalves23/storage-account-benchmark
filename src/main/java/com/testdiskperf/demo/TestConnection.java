package com.testdiskperf.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testdiskperf.demo.shared.FileReaderService;
import java.nio.file.Path;
import java.io.PrintWriter;
import java.io.StringWriter;


@RestController
public class TestConnection {
    private final FileReaderService service;

    public TestConnection(FileReaderService service) {
        this.service = service;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        try {
            String basePath = "./src/main/resources/static/data_mock_small.json";
            String outDir = "./src/main/resources/results/small-data";
            Path base = Path.of(basePath);
            Path out = Path.of(outDir);
            service.saveFilesInDisk(base, out);
            return ResponseEntity.ok("Arquivos criados com sucesso");
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));

            return ResponseEntity.status(500).body("Erro: " + e.getMessage() + "\n\n\n\n" + sw.toString() + "\n\n\n\n" + Path.of(".").toAbsolutePath());
        }
    }

}