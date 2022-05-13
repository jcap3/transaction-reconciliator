package com.caponong.transactionreconciliator.services;

import com.caponong.transactionreconciliator.enums.ReconciliationRequestStatus;
import com.caponong.transactionreconciliator.model.ReconciliationRequestDetails;

public interface ReconciliationRequestHandlerService {
    
    void addRequestToken(String token, String fileName1, String fileName2);
    
    void retireExpiredTokens();
    
    void updateStatus(String token, ReconciliationRequestStatus status);

    ReconciliationRequestDetails getDetails(String token);
    
}
