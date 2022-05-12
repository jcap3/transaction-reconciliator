package com.caponong.transactionreconciliator.converter;

import com.caponong.transactionreconciliator.entity.Transaction;
import com.caponong.transactionreconciliator.model.TransactionModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TransactionEntityToModelConverter implements Converter<Transaction, TransactionModel> {

    @Override
    public TransactionModel convert(Transaction source) {
        return TransactionModel.builder()
                .profileName(source.getProfileName())
                .transactionDate(source.getTransactionDate())
                .transactionAmount(source.getTransactionAmount())
                .transactionNarrative(source.getTransactionNarrative())
                .transactionDescription(source.getTransactionDescription())
                .transactionId(source.getTransactionId())
                .transactionType(source.getTransactionType())
                .walletReference(source.getWalletReference())
                .build();
        
    }
}
