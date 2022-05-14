package com.caponong.transactionreconciliator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionModel {

    private String profileName;

    private String transactionDate;

    private String transactionAmount;

    private String transactionNarrative;

    private String transactionDescription;

    private String transactionId;

    private String transactionType;

    private String walletReference;
}
