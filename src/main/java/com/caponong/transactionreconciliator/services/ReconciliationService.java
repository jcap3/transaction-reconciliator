package com.caponong.transactionreconciliator.services;

import com.caponong.transactionreconciliator.model.MatchTransactionsCountResponse;
import com.caponong.transactionreconciliator.model.TransactionsUploadResponse;
import com.caponong.transactionreconciliator.model.UnmatchedTransactionsResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ReconciliationService {

    TransactionsUploadResponse uploadTransaction(MultipartFile firstTransactionSet, MultipartFile secondTransactionSet);

    MatchTransactionsCountResponse getMatchSummary(String reconciliationToken);
    
    UnmatchedTransactionsResponse getUnmatchedTransactions(String reconciliationToken);
}
