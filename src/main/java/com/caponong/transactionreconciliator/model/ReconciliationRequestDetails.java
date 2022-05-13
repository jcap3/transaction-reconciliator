package com.caponong.transactionreconciliator.model;

import com.caponong.transactionreconciliator.enums.ReconciliationRequestStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class  ReconciliationRequestDetails {

    private String fileName1;
    
    private String fileName2; 
    
    private LocalDateTime creationDate;
    
    private ReconciliationRequestStatus status;
}
