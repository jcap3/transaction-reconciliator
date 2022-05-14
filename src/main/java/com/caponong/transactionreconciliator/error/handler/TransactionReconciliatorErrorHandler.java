package com.caponong.transactionreconciliator.error.handler;

import com.caponong.transactionreconciliator.error.TransactionReconciliatorErrorCodes;
import com.caponong.transactionreconciliator.error.exception.InvalidFile;
import com.caponong.transactionreconciliator.error.exception.RequestInterruptedError;
import com.caponong.transactionreconciliator.error.exception.RequestNotReadyError;
import com.caponong.transactionreconciliator.error.exception.RequestTokenNotFound;
import com.caponong.transactionreconciliator.model.Response;
import com.caponong.transactionreconciliator.util.ResponseConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@Slf4j
@ControllerAdvice
public class TransactionReconciliatorErrorHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
                                                                     HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, ResponseConverter.convertError(TransactionReconciliatorErrorCodes.MISSING_CSV_FILE_REQUEST), headers, status, request);
    }

    @ExceptionHandler(InvalidFile.class)
    public ResponseEntity<Object> handleInvalidFileError(InvalidFile ex, WebRequest request) {
        log.error(ex.getMessage());
        return handleExceptionInternal(ex, ResponseConverter.convertError(TransactionReconciliatorErrorCodes.INVALID_FILE_UPLOADED), new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleFieldValidation(ConstraintViolationException ex, WebRequest request) {
        log.error(ex.getMessage());
        return handleExceptionInternal(ex, ResponseConverter.convertError(TransactionReconciliatorErrorCodes.INVALID_RECONCILIATION_TOKEN), new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(RequestNotReadyError.class)
    public ResponseEntity<Object> handleRequestNotReady(RequestNotReadyError ex, WebRequest request) {
        log.error(ex.getMessage());
        return handleExceptionInternal(ex, ResponseConverter.convertError(TransactionReconciliatorErrorCodes.REQUEST_NOT_YET_READY), new HttpHeaders(),
                HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    @ExceptionHandler(RequestInterruptedError.class)
    public ResponseEntity<Object> handleRequestTokenInterrupted(RequestInterruptedError ex, WebRequest request) {
        log.error(ex.getMessage());
        return handleExceptionInternal(ex, ResponseConverter.convertError(TransactionReconciliatorErrorCodes.REQUEST_ERROR_OUT), new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(RequestTokenNotFound.class)
    public ResponseEntity<Object> handleRequestTokenNotFound(RequestTokenNotFound ex, WebRequest request) {
        log.error(ex.getMessage());
        return handleExceptionInternal(ex, ResponseConverter.convertError(TransactionReconciliatorErrorCodes.REQUEST_NOT_FOUND), new HttpHeaders(),
                HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntime(Exception ex, WebRequest request) {
        log.error(ex.getMessage());
        ex.printStackTrace();
        return handleExceptionInternal(ex, ResponseConverter.convertError(TransactionReconciliatorErrorCodes.INTERNAL_ERROR), new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
