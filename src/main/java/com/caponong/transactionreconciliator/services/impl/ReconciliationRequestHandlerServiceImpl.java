package com.caponong.transactionreconciliator.services.impl;

import com.caponong.transactionreconciliator.model.ReconciliationRequestDetails;
import com.caponong.transactionreconciliator.services.ReconciliationRequestHandlerService;
import com.caponong.transactionreconciliator.services.TransactionsDbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ReconciliationRequestHandlerServiceImpl implements ReconciliationRequestHandlerService {

    @Autowired
    private TransactionsDbService transactionsDbService;

    @Value("#{${reconciliaton-token-expiry}}")
    private int tokenExpiry;

    private final Map<String, ReconciliationRequestDetails> reconciliationTokensMap = new ConcurrentHashMap<>();

    @Override
    public synchronized void addRequestToken(String token, String fileName1, String fileName2) {
        reconciliationTokensMap.put(token,
                ReconciliationRequestDetails.builder()
                        .isReadyForProcessing(Boolean.FALSE)
                        .fileName1(fileName1)
                        .fileName2(fileName2)
                        .creationDate(LocalDateTime.now())
                        .build());
    }

    @Override
    public synchronized void retireExpiredTokens() {
        log.info(reconciliationTokensMap.toString());
        getExpiredTokens().forEach(expiredToken -> {
            log.info("Retiring expired token: {}", expiredToken);
            reconciliationTokensMap.remove(expiredToken);
            transactionsDbService.deleteTransactionsByToken(expiredToken);
        });
    }

    @Override
    public void activateForProcessing(String token) {
        log.info("Token: {} - Ready for processing", token);
        reconciliationTokensMap.get(token).setReadyForProcessing(Boolean.TRUE);
    }

    @Override
    public ReconciliationRequestDetails getDetails(String token) {
        ReconciliationRequestDetails source = reconciliationTokensMap.get(token);
        return ReconciliationRequestDetails.builder()
                .fileName1(source.getFileName1())
                .fileName2(source.getFileName2())
                .creationDate(source.getCreationDate())
                .isReadyForProcessing(source.isReadyForProcessing())
                .build();
    }

    private List<String> getExpiredTokens() {
        List<String> expiredTokens = new ArrayList<>();

        reconciliationTokensMap.forEach((token, requestStatus) -> {
            if (isTokenExpired(requestStatus.getCreationDate()))
                expiredTokens.add(token);
        });

        return expiredTokens;
    }

    private boolean isTokenExpired(LocalDateTime creationDate) {
        return LocalDateTime.now().isAfter(creationDate.plusSeconds(tokenExpiry));
    }
}
