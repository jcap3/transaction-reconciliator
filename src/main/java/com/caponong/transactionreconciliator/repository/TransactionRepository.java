package com.caponong.transactionreconciliator.repository;

import com.caponong.transactionreconciliator.entity.Transaction;
import com.caponong.transactionreconciliator.entity.TransactionKey;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, TransactionKey> {

    void deleteByReconciliationTokenContaining(String token);

    @Query(value = "select count(a.transaction_id) as total_match\n" +
            "\n" +
            "from (select * from transaction where reconciliation_token like ?1) a\n" +
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
    int getMatchedTransactionsCount(String likeExpressionFile1, String likeExpressionFile2);

    @Query(value = "select a.*\n" +
            "\n" +
            "from (select * from transaction where reconciliation_token like ?1) a\n" +
            "\n" +
            "left join (select * from transaction where reconciliation_token like ?2) b\n" +
            "\n" +
            "on a.transaction_id = b.transaction_id and \n" +
            "a.transaction_description = b.transaction_description and \n" +
            "a.profile_name = b.profile_name and\n" +
            "a.transaction_amount = b.transaction_amount AND \n" +
            "a.transaction_date = b.transaction_date and \n" +
            "a.transaction_narrative = b.transaction_narrative and \n" +
            "a.transaction_type = b.transaction_type and \n" +
            "a.wallet_reference = b.wallet_reference\n" +
            "where \n" +
            "b.transaction_id is null", nativeQuery = true)
    List<Transaction> getFirstFileUnMatchedTransactions(String likeExpressionFile1, String likeExpressionFile2);

    @Query(value = "select b.*\n" +
            "\n" +
            "from (select * from transaction where reconciliation_token like ?1) a\n" +
            "\n" +
            "right join (select * from transaction where reconciliation_token like ?2) b\n" +
            "\n" +
            "on a.transaction_id = b.transaction_id and \n" +
            "a.transaction_description = b.transaction_description and \n" +
            "a.profile_name = b.profile_name and\n" +
            "a.transaction_amount = b.transaction_amount AND \n" +
            "a.transaction_date = b.transaction_date and \n" +
            "a.transaction_narrative = b.transaction_narrative and \n" +
            "a.transaction_type = b.transaction_type and \n" +
            "a.wallet_reference = b.wallet_reference\n" +
            "where \n" +
            "a.transaction_id is null", nativeQuery = true)
    List<Transaction> getSecondFileUnMatchedTransactions(String likeExpressionFile1, String likeExpressionFile2);

    @Query(value = "select count(a.transaction_id) as total_unique_transactions from transaction a where " +
            "reconciliation_token like ?1", nativeQuery = true)
    int getTotalUniqueTransactions(String likeExpression);
    
    List<Transaction> findByReconciliationTokenContaining(String token, Pageable page);

}
