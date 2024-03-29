package com.caponong.transactionreconciliator.services.impl;

import com.caponong.transactionreconciliator.advice.ReconciliationTokenConsumer;
import com.caponong.transactionreconciliator.error.exception.InternalServerError;
import com.caponong.transactionreconciliator.model.*;
import com.caponong.transactionreconciliator.model.MatchTransactionsCountResponse.FileMatchResult;
import com.caponong.transactionreconciliator.model.UnmatchedTransactionsResponse.UnmatchedTransactionsWrapper;
import com.caponong.transactionreconciliator.services.ReconciliationRequestHandlerService;
import com.caponong.transactionreconciliator.services.ReconciliationService;
import com.caponong.transactionreconciliator.services.TransactionsReader;
import com.caponong.transactionreconciliator.services.Writer;
import com.caponong.transactionreconciliator.util.CsvFileUtil;
import com.caponong.transactionreconciliator.util.ReconciliationTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.caponong.transactionreconciliator.constant.TransactionsConstant.FIRST_TRANSACTION_IDENTIFIER;
import static com.caponong.transactionreconciliator.constant.TransactionsConstant.SECOND_TRANSACTION_IDENTIFIER;
import static java.util.UUID.randomUUID;

@Slf4j
@Service
public class ReconciliationServiceImpl implements ReconciliationService {

    @Autowired
    private Writer<CsvFile> databaseWriter;

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
        
        File firstTransactionSetFile = CsvFileUtil.writeMultiPartFileToCreatedFile(firstTransactionSet, 
                ReconciliationTokenUtil.addIdentifier(generatedReconciliationToken, FIRST_TRANSACTION_IDENTIFIER));
        File secondTransactionSetFile = CsvFileUtil.writeMultiPartFileToCreatedFile(secondTransactionSet, 
                ReconciliationTokenUtil.addIdentifier(generatedReconciliationToken, SECOND_TRANSACTION_IDENTIFIER));

        databaseWriter.write(new CsvFile(firstTransactionSetFile, FIRST_TRANSACTION_IDENTIFIER + generatedReconciliationToken));
        databaseWriter.write(new CsvFile(secondTransactionSetFile, SECOND_TRANSACTION_IDENTIFIER + generatedReconciliationToken));

        return TransactionsUploadResponse.builder()
                .reconciliationToken(generatedReconciliationToken)
                .tokenExpiry(tokenExpiry)
                .build();
    }

    @ReconciliationTokenConsumer
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
                    .firstTransactionSet((FileMatchResult) waitForFutureResult(file1))
                    .secondTransactionSet((FileMatchResult) waitForFutureResult(file2))
                    .build();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Service error", e);
            Thread.currentThread().interrupt();
            throw new InternalServerError(e);
        }
    }

    @ReconciliationTokenConsumer
    @Override
    @SuppressWarnings("unchecked")
    public UnmatchedTransactionsResponse getUnmatchedTransactions(String reconciliationToken) {
        
        ReconciliationRequestDetails reconciliationRequestDetails = reconciliationRequestHandlerService.getDetails(reconciliationToken);
        
        try {
            Future<List<UnmatchedTransaction>> futureUnmatchedTransactionsOne =
                    transactionReconciliator.getUnmatchedTransactionSummary(FIRST_TRANSACTION_IDENTIFIER, reconciliationToken);
            Future<List<UnmatchedTransaction>> futureUnmatchedTransactionsTwo =
                    transactionReconciliator.getUnmatchedTransactionSummary(SECOND_TRANSACTION_IDENTIFIER, reconciliationToken);
            
            UnmatchedTransactionsWrapper wrapperOne = UnmatchedTransactionsWrapper.builder()
                    .fileName(reconciliationRequestDetails.getFileName1())
                    .unmatchedTransactions((List<UnmatchedTransaction>)waitForFutureResult(futureUnmatchedTransactionsOne))
                    .build();

            UnmatchedTransactionsWrapper wrapperTwo = UnmatchedTransactionsWrapper.builder()
                    .fileName(reconciliationRequestDetails.getFileName2())
                    .unmatchedTransactions((List<UnmatchedTransaction>)waitForFutureResult(futureUnmatchedTransactionsTwo))
                    .build();
            
            return UnmatchedTransactionsResponse.builder()
                    .firstTransactionSet(wrapperOne)
                    .secondTransactionSet(wrapperTwo)
                    .build();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Service error", e);
            Thread.currentThread().interrupt();
            throw new InternalServerError(e);
        }
    }

    private <T> Object waitForFutureResult(Future<T> futureTask) throws ExecutionException, InterruptedException {
        while (true) {
            if (futureTask.isDone()) {
                return futureTask.get();
            }
        }
    }
    
}
