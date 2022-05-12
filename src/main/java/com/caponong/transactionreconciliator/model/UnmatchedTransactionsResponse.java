package com.caponong.transactionreconciliator.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UnmatchedTransactionsResponse {

    private UnmatchedTransactionsWrapper firstTransactionSet;

    private UnmatchedTransactionsWrapper secondTransactionSet;

    @Data
    @Builder
    public static class UnmatchedTransactionsWrapper {

        private List<UnmatchedTransaction> unmatchedTransactions;
    }
}
