package com.caponong.transactionreconciliator.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReconciliationRequestDetails {

    private String fileName1;
    
    private String fileName2; 
    
    private LocalDateTime creationDate;
    
    private boolean isReadyForProcessing;
}
