package com.caponong.transactionreconciliator.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static com.caponong.transactionreconciliator.util.CsvFileUtil.createReconciliationDirectory;

@Component
public class StartUpComponent implements CommandLineRunner {
    
    @Override
    public void run(String... args) throws Exception {
        createReconciliationDirectory();
    }
}
