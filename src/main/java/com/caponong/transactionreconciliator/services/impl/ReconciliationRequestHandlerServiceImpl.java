package com.caponong.transactionreconciliator.services.impl;

import com.caponong.transactionreconciliator.enums.ReconciliationRequestStatus;
import com.caponong.transactionreconciliator.error.exception.RequestTokenNotFound;
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
                        .status(ReconciliationRequestStatus.NOT_READY)
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
    public void updateStatus(String token, ReconciliationRequestStatus status) {
        log.info("Token: {} - Changed status to {}", token, status);
        reconciliationTokensMap.get(token).setStatus(status);
    }

    @Override
    public synchronized ReconciliationRequestDetails getDetails(String token) {
        if (!reconciliationTokensMap.containsKey(token))
            throw new RequestTokenNotFound("Request token not found");
        ReconciliationRequestDetails source = reconciliationTokensMap.get(token);
        return ReconciliationRequestDetails.builder()
                .fileName1(source.getFileName1())
                .fileName2(source.getFileName2())
                .creationDate(source.getCreationDate())
                .status(source.getStatus())
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
