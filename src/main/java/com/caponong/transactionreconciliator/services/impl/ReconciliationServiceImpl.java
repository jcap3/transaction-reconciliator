package com.caponong.transactionreconciliator.services.impl;

import com.caponong.transactionreconciliator.model.MatchTransactionsCountResponse;
import com.caponong.transactionreconciliator.model.MultipartCsvFile;
import com.caponong.transactionreconciliator.model.ReconciliationRequestDetails;
import com.caponong.transactionreconciliator.model.TransactionsUploadResponse;
import com.caponong.transactionreconciliator.services.ReconciliationRequestHandlerService;
import com.caponong.transactionreconciliator.services.ReconciliationService;
import com.caponong.transactionreconciliator.services.TransactionsDbService;
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
    
    @Autowired
    private TransactionsDbService transactionsDbService;

    private static final String FIRST_TRANSACTION_IDENTIFIER = "a_";

    private static final String SECOND_TRANSACTION_IDENTIFIER = "b_";

    @Value("#{${reconciliaton-token-expiry}}")
    private int tokenExpiry;

    @Override
    public TransactionsUploadResponse uploadTransaction(MultipartFile firstTransactionSet,
                                                        MultipartFile secondTransactionSet) {

        String generatedReconciliationToken = String.valueOf(randomUUID());
        reconciliationRequestHandlerService.addRequestToken(generatedReconciliationToken, 
                firstTransactionSet.getOriginalFilename(), secondTransactionSet.getOriginalFilename());
        
        databaseWriter.write(new MultipartCsvFile(firstTransactionSet, FIRST_TRANSACTION_IDENTIFIER + generatedReconciliationToken));
        databaseWriter.write(new MultipartCsvFile(secondTransactionSet, SECOND_TRANSACTION_IDENTIFIER + generatedReconciliationToken));
        
        return TransactionsUploadResponse.builder()
                .reconciliationToken(generatedReconciliationToken)
                .tokenExpiry(tokenExpiry)
                .build();
    }

    @Override
    public MatchTransactionsCountResponse getMatchCount(String reconciliationToken) {
        ReconciliationRequestDetails reconciliationRequestDetails = reconciliationRequestHandlerService.getDetails(reconciliationToken);
        
        int matchCount = transactionsDbService.getMatchCount(FIRST_TRANSACTION_IDENTIFIER, SECOND_TRANSACTION_IDENTIFIER, reconciliationToken);
        int totalTransactionFile1 = transactionsDbService.getTotalUniqueTransaction(FIRST_TRANSACTION_IDENTIFIER, reconciliationToken);
        int totalTransactionFile2 = transactionsDbService.getTotalUniqueTransaction(SECOND_TRANSACTION_IDENTIFIER, reconciliationToken);
        int unMatchedTransactionFile1 = Math.max(totalTransactionFile1 - matchCount, 0);
        int unMatchedTransactionFile2 = Math.max(totalTransactionFile2 - matchCount, 0);
        
        MatchTransactionsCountResponse.FileMatchResult file1 = buildFileMatchResult(matchCount, totalTransactionFile1, 
                unMatchedTransactionFile1, reconciliationRequestDetails.getFileName1());

        MatchTransactionsCountResponse.FileMatchResult file2 = buildFileMatchResult(matchCount, totalTransactionFile2, 
                unMatchedTransactionFile2, reconciliationRequestDetails.getFileName2());
        
        return MatchTransactionsCountResponse.builder()
                .file1(file1)
                .file2(file2)
                .build();
    }

    private MatchTransactionsCountResponse.FileMatchResult buildFileMatchResult(int matchCount, int totalTransactionFile2, 
                                                                                int unMatchedTransactionFile2, String fileName2) {
        return MatchTransactionsCountResponse.FileMatchResult.builder()
                .fileName(fileName2)
                .matchedTransactions(matchCount)
                .totalUniqueRecords(totalTransactionFile2)
                .unmatchedTransactions(unMatchedTransactionFile2)
                .build();
    }

}
