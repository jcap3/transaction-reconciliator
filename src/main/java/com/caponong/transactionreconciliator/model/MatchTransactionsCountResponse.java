package com.caponong.transactionreconciliator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchTransactionsCountResponse {

    private FileMatchResult firstTransactionSet;
    
    private FileMatchResult secondTransactionSet;
    
    @Data
    @Builder
    public static class FileMatchResult {
        
        private String fileName;
        
        private Integer totalUniqueRecords;
        
        private Integer matchedTransactions;

        private Integer unmatchedTransactions;    
    }
}