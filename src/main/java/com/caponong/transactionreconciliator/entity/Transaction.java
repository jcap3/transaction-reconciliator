package com.caponong.transactionreconciliator.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@IdClass(TransactionKey.class)
public class Transaction {

    private String profileName;

    private Date transactionDate;

    private BigDecimal transactionAmount;

    private String transactionNarrative;

    @Id
    private String transactionDescription;

    @Id
    private String transactionId;

    private String transactionType;

    private String walletReference;

    @Id
    private String reconciliationToken;
}
