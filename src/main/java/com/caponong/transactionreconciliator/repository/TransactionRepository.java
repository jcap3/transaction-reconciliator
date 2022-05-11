package com.caponong.transactionreconciliator.repository;

import com.caponong.transactionreconciliator.entity.TransactionKey;
import com.caponong.transactionreconciliator.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, TransactionKey> {

    
    void deleteByReconciliationTokenContaining(String token);
    
}
