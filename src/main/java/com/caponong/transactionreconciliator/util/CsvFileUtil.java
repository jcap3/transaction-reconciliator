package com.caponong.transactionreconciliator.util;

import com.caponong.transactionreconciliator.error.exception.InternalServerError;
import com.caponong.transactionreconciliator.error.exception.InvalidFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static com.caponong.transactionreconciliator.constant.TransactionsConstant.*;

@Slf4j
public class CsvFileUtil {
    
    private CsvFileUtil () {}
    
    public static void checkIfFileUploadedIsCsv(MultipartFile multipartFile) {
        if (!Objects.requireNonNull(multipartFile.getOriginalFilename()).endsWith(".csv")) {
            throw new InvalidFile("Invalid file uploaded");
        }
    }
    
    public static File writeMultiPartFileToCreatedFile (MultipartFile sourceMultiPartFile, 
                                                        String generatedReconciliationTokenWithIdentifier) {
        File file = new File(StringUtils.join(new String[]{System.getProperty(JAVA_TEMP_FOLDER), RECONCILIATION_TOKEN_FOLDER, 
                generatedReconciliationTokenWithIdentifier}));
        try (OutputStream os1 = new FileOutputStream(file)) {
            os1.write(sourceMultiPartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    
    public static void deleteFileByReconciliationToken (String reconciliationToken) {
        File fileToDelete1 = new File(StringUtils.join(new String[]{System.getProperty(JAVA_TEMP_FOLDER), RECONCILIATION_TOKEN_FOLDER,
                ReconciliationTokenUtil.addIdentifier(reconciliationToken, FIRST_TRANSACTION_IDENTIFIER)}));
        File fileToDelete2 = new File(StringUtils.join(new String[]{System.getProperty(JAVA_TEMP_FOLDER), RECONCILIATION_TOKEN_FOLDER,
                ReconciliationTokenUtil.addIdentifier(reconciliationToken, SECOND_TRANSACTION_IDENTIFIER)}));

        try {
            Files.delete(fileToDelete1.toPath());
            Files.delete(fileToDelete2.toPath());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new InternalServerError(e);
        }
        
    }

    public static void createReconciliationDirectory() throws IOException {
        Files.createDirectories(Paths.get(StringUtils.join(new String[]{System.getProperty(JAVA_TEMP_FOLDER), RECONCILIATION_TOKEN_FOLDER})));
    }
}
