package com.caponong.transactionreconciliator.services.impl;

import com.caponong.transactionreconciliator.services.ReconciliationRequestHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RetireExpiredTokenScheduledTask {

    @Autowired
    ReconciliationRequestHandlerService reconciliationRequestHandlerService;

    @Async("expiredTokensExecutor")
    @Scheduled(fixedRateString = "${expired-reconciliaton-token-check-interval}")
    public void run() {
        reconciliationRequestHandlerService.retireExpiredTokens();
    }
}
