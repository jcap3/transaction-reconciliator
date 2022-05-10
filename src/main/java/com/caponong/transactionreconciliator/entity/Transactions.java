package com.caponong.transactionreconciliator.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@IdClass(TransactionKey.class)
public class Transactions {

    private String profileName;
    
    private LocalDateTime transactionDate;

    private BigDecimal transactionAmount;

    private String transactionNarrative;

    @Id
    private String transactionDescription;

    @Id
    private String transactionId;
    
    private String transactionType;

    private String walletReference;
}
