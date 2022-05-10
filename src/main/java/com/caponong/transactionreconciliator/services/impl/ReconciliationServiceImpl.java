package com.caponong.transactionreconciliator.services.impl;

import com.caponong.transactionreconciliator.model.TransactionsUploadResponse;
import com.caponong.transactionreconciliator.services.ReconciliationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class ReconciliationServiceImpl implements ReconciliationService {

    @Override
    public TransactionsUploadResponse uploadTransaction(MultipartFile firstTransactionSet, MultipartFile secondTransactionSet) {
        return null;
    }
}
