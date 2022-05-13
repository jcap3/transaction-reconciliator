package com.caponong.transactionreconciliator.util;

import com.caponong.transactionreconciliator.error.exception.InvalidFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class CsvFileUtil {
    
    private CsvFileUtil () {}
    
    public static void checkIfFileUploadedIsCsv(MultipartFile multipartFile) {
        if (!Objects.requireNonNull(multipartFile.getOriginalFilename()).endsWith(".csv")) {
            throw new InvalidFile("Invalid file uploaded");
        }
    }

}
