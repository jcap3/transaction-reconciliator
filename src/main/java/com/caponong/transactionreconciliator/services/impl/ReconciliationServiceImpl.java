package com.caponong.transactionreconciliator.services.impl;

import com.caponong.transactionreconciliator.error.exception.InternalServerError;
import com.caponong.transactionreconciliator.model.MatchTransactionsCountResponse;
import com.caponong.transactionreconciliator.model.MatchTransactionsCountResponse.FileMatchResult;
import com.caponong.transactionreconciliator.model.MultipartCsvFile;
import com.caponong.transactionreconciliator.model.ReconciliationRequestDetails;
import com.caponong.transactionreconciliator.model.TransactionsUploadResponse;
import com.caponong.transactionreconciliator.services.ReconciliationRequestHandlerService;
import com.caponong.transactionreconciliator.services.ReconciliationService;
import com.caponong.transactionreconciliator.services.TransactionsReader;
import com.caponong.transactionreconciliator.services.Writer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.caponong.transactionreconciliator.constant.TransactionsConstant.FIRST_TRANSACTION_IDENTIFIER;
import static com.caponong.transactionreconciliator.constant.TransactionsConstant.SECOND_TRANSACTION_IDENTIFIER;
import static java.util.UUID.randomUUID;

@Slf4j
@Service
public class ReconciliationServiceImpl implements ReconciliationService {

    @Autowired
    private Writer<MultipartCsvFile> databaseWriter;
    
    @Autowired
    private ReconciliationRequestHandlerService reconciliationRequestHandlerService;
    
    @Autowired
    private TransactionsReader transactionReconciliator;

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
    public MatchTransactionsCountResponse getMatchSummary(String reconciliationToken) {
        ReconciliationRequestDetails reconciliationRequestDetails = reconciliationRequestHandlerService.getDetails(reconciliationToken);
        
        try {
            Future<FileMatchResult> file1 = transactionReconciliator.getFileMatchResult(reconciliationRequestDetails.getFileName1(),
                    FIRST_TRANSACTION_IDENTIFIER,
                    reconciliationToken);
            Future<FileMatchResult> file2 = transactionReconciliator.getFileMatchResult(reconciliationRequestDetails.getFileName2(), 
                    SECOND_TRANSACTION_IDENTIFIER,
                    reconciliationToken);

            return MatchTransactionsCountResponse.builder()
                    .firstTransactionSet(waitForFutureResult(file1))
                    .secondTransactionSet(waitForFutureResult(file2))
                    .build();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Service error", e);
            Thread.currentThread().interrupt();
            throw new InternalServerError(e);
        } 
    }
    
    private FileMatchResult waitForFutureResult(Future<FileMatchResult> futureTask) throws ExecutionException, InterruptedException {
        while (true) {
            if (futureTask.isDone()){
                return futureTask.get();
            }
        }
    }
}
