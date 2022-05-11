package com.caponong.transactionreconciliator.services.impl;

import com.caponong.transactionreconciliator.model.MultipartCsvFile;
import com.caponong.transactionreconciliator.model.TransactionsUploadResponse;
import com.caponong.transactionreconciliator.services.ReconciliationRequestHandlerService;
import com.caponong.transactionreconciliator.services.ReconciliationService;
import com.caponong.transactionreconciliator.services.Writer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static java.util.UUID.randomUUID;

@Slf4j
@Service
public class ReconciliationServiceImpl implements ReconciliationService {

    @Autowired
    private Writer<MultipartCsvFile> databaseWriter;
    
    @Autowired
    private ReconciliationRequestHandlerService reconciliationRequestHandlerService;

    private static final String FIRST_TRANSACTION_IDENTIFIER = "a_";

    private static final String SECOND_TRANSACTION_IDENTIFIER = "b_";

    @Value("#{${reconciliaton-token-expiry}}")
    private int tokenExpiry;

    @Override
    public TransactionsUploadResponse uploadTransaction(MultipartFile firstTransactionSet,
                                                        MultipartFile secondTransactionSet) {

        String generatedReconciliationToken = String.valueOf(randomUUID());
        reconciliationRequestHandlerService.addRequestToken(generatedReconciliationToken);
        databaseWriter.write(new MultipartCsvFile(firstTransactionSet, FIRST_TRANSACTION_IDENTIFIER + generatedReconciliationToken));
        databaseWriter.write(new MultipartCsvFile(secondTransactionSet, SECOND_TRANSACTION_IDENTIFIER + generatedReconciliationToken));
        
        return TransactionsUploadResponse.builder()
                .reconciliationToken(generatedReconciliationToken)
                .tokenExpiry(tokenExpiry)
                .build();
    }

}
