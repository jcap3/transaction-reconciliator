package com.caponong.transactionreconciliator.services;

import com.caponong.transactionreconciliator.model.ReconciliationRequestDetails;

public interface ReconciliationRequestHandlerService {
    
    void addRequestToken(String token, String fileName1, String fileName2);
    
    void retireExpiredTokens();
    
    void activateForProcessing(String token);

    ReconciliationRequestDetails getDetails(String token);
    
}
