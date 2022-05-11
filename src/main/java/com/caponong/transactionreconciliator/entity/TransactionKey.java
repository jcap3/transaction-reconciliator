package com.caponong.transactionreconciliator.entity;

import lombok.EqualsAndHashCode;

import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode
public class TransactionKey implements Serializable {

    @Id
    private String transactionId;

    @Id
    private String transactionDescription;
    
    @Id
    private String reconciliationToken;
}
