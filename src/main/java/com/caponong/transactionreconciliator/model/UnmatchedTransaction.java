package com.caponong.transactionreconciliator.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UnmatchedTransaction {

    private TransactionModel transaction;

    private List<TransactionModel> potentialMatches;
}
