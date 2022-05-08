package com.caponong.transactionreconciliator.model;

import lombok.Data;

@Data
public class TransactionsUploadResponse {

    private String reconciliationToken;
    
    private Integer tokenExpiry;
}
