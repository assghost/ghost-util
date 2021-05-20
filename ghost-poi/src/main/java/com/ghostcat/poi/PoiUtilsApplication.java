package com.ghostcat.poi;

import com.ghostcat.common.util.GhostDateTimeUtils;
import com.ghostcat.poi.reader.ExcelReader;
import com.ghostcat.poi.writer.GhostExcelWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.time.LocalDateTime;

@RestController
@SpringBootApplication
public class PoiUtilsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PoiUtilsApplication.class, args);
    }

    @GetMapping("ghost/util/poi/download")
    public ResponseEntity<byte[]> download() {
        try {
            File file = new File(ExcelReader.class.getClassLoader().getResource("excel-tmp/simple-tmp.xlsx").getPath());

            String fileName = String.format("download-%s.xlsx", GhostDateTimeUtils.date2Str(LocalDateTime.now()));

            byte[] bytes = GhostExcelWriter.readFileToByte(file);
            return GhostExcelWriter.buildDownloadResponse(bytes, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
