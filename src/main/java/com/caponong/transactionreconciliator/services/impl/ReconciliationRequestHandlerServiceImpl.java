package com.caponong.transactionreconciliator.services.impl;

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

    private final Map<String, LocalDateTime> reconciliationTokensMap = new ConcurrentHashMap<>();

    @Override
    public void addRequestToken(String token) {
        reconciliationTokensMap.put(token, LocalDateTime.now());
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

    private List<String> getExpiredTokens() {
        List<String> expiredTokens = new ArrayList<>();

        reconciliationTokensMap.forEach((token, creationDate) -> {
            if (isTokenExpired(creationDate))
                expiredTokens.add(token);
        });

        return expiredTokens;
    }

    private boolean isTokenExpired(LocalDateTime creationDate) {
        return LocalDateTime.now().isAfter(creationDate.plusSeconds(tokenExpiry));
    }
}
