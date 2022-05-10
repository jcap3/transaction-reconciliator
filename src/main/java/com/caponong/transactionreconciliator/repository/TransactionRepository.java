package com.caponong.transactionreconciliator.repository;

import com.caponong.transactionreconciliator.entity.TransactionKey;
import com.caponong.transactionreconciliator.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, TransactionKey> {

}
