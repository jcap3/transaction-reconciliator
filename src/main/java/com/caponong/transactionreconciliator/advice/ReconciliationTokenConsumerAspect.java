package com.caponong.transactionreconciliator.advice;

import com.caponong.transactionreconciliator.enums.ReconciliationRequestStatus;
import com.caponong.transactionreconciliator.error.exception.RequestInterruptedError;
import com.caponong.transactionreconciliator.error.exception.RequestNotReadyError;
import com.caponong.transactionreconciliator.services.ReconciliationRequestHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ReconciliationTokenConsumerAspect {
    
    @Autowired
    private ReconciliationRequestHandlerService reconciliationRequestHandlerService;
    
    @Before("@annotation(annotation) && args(..,@RequestBody payload)")
    public void checkToken(JoinPoint joinPoint, ReconciliationTokenConsumer annotation, Object payload) {
        log.info("Checking reconciliation token: {}", payload);
        verifyRequestStatus(reconciliationRequestHandlerService.getDetails((String) payload).getStatus());
    }

    private void verifyRequestStatus(ReconciliationRequestStatus status) {
        if (status == ReconciliationRequestStatus.NOT_READY)
            throw new RequestNotReadyError("Requested token not yet ready");
        else if (status == ReconciliationRequestStatus.ERROR)
            throw new RequestInterruptedError("Request token encountered an error");
        
    }
}
