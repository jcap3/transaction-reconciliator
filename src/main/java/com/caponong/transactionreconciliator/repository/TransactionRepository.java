package com.caponong.transactionreconciliator.repository;

import com.caponong.transactionreconciliator.entity.TransactionKey;
import com.caponong.transactionreconciliator.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, TransactionKey> {

    
    void deleteByReconciliationTokenContaining(String token);
    
    @Query (value = "select count(a.transaction_id) as total_match\n" +
            "\n" +
            "from (select a.* from transaction a where reconciliation_token like ?1) a\n" +
            "\n" +
            "inner join (select * from transaction where reconciliation_token like ?2) b\n" +
            "\n" +
            "on a.transaction_id = b.transaction_id and \n" +
            "a.transaction_description = b.transaction_description and \n" +
            "a.profile_name = b.profile_name and\n" +
            "a.transaction_amount = b.transaction_amount AND \n" +
            "a.transaction_date = b.transaction_date and \n" +
            "a.transaction_narrative = b.transaction_narrative and \n" +
            "a.transaction_type = b.transaction_type and \n" +
            "a.wallet_reference = b.wallet_reference", nativeQuery = true)
    Integer getMatchedTransactionsCount (String likeExpressionFile1, String likeExpressionFile2);
    
    @Query(value = "select count(a.transaction_id) as total_unique_transactions from transaction a where " +
            "reconciliation_token like ?1", nativeQuery = true)
    Integer getTotalUniqueTransactions(String likeExpression);
    
}
