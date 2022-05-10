package com.caponong.transactionreconciliator.entity;

import javax.persistence.Id;
import java.io.Serializable;

public class TransactionKey implements Serializable {

    @Id
    private String transactionId;

    @Id
    private String transactionDescription;
}
