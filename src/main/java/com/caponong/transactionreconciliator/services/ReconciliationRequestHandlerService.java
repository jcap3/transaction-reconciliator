package com.caponong.transactionreconciliator.services;

public interface ReconciliationRequestHandlerService {
    
    void addRequestToken(String token);
    
    void retireExpiredTokens();
    
    void activateForProcessing(String token);
}
