package com.caponong.transactionreconciliator.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReconciliationRequestStatus {

    private LocalDateTime creationDate;
    
    private boolean isReadyForProcessing;
}
