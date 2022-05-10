package com.caponong.transactionreconciliator.services;

import com.caponong.transactionreconciliator.model.TransactionsUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ReconciliationService {

    TransactionsUploadResponse uploadTransaction(MultipartFile firstTransactionSet, MultipartFile secondTransactionSet);
}
