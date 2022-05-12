package com.caponong.transactionreconciliator.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class TransactionModel {

    private String profileName;

    private Date transactionDate;

    private BigDecimal transactionAmount;

    private String transactionNarrative;

    private String transactionDescription;

    private String transactionId;

    private String transactionType;

    private String walletReference;
}
