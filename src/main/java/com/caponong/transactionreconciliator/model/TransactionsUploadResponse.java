package com.caponong.transactionreconciliator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionsUploadResponse {
    
    private String reconciliationToken;
    
    private int tokenExpiry;
}
