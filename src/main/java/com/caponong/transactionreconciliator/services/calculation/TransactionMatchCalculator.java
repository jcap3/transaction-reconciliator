package com.caponong.transactionreconciliator.services.calculation;

import com.caponong.transactionreconciliator.entity.Transaction;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@Service
public class TransactionMatchCalculator {
    
    private final List<TransactionFieldChecker> checkers;

    @Value("#{${potential-match-difference-threshold}}")
    private int potentialMatchDifferenceThreshold;

    public TransactionMatchCalculator () {
        checkers = new ArrayList<>();
        checkers.add(new ProfileNameChecker());
        checkers.add(new TransactionDateChecker());
        checkers.add(new TransactionAmountChecker());
        checkers.add(new TransactionNarrativeChecker());
        checkers.add(new TransactionDescriptionChecker());
        checkers.add(new TransactionIdChecker());
        checkers.add(new TransactionTypeChecker());
        checkers.add(new WalletReferenceChecker());
    }
    
    public boolean isPossibleMatch (Transaction transactionOne, Transaction transactionTwo) {
        int differences = (int) checkers.stream().filter(checker -> 
                !checker.isFieldMatch(transactionOne, transactionTwo)).count();
        return differences <= potentialMatchDifferenceThreshold;
    }
}
