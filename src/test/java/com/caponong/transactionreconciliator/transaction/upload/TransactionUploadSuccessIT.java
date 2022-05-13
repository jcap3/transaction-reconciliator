package com.caponong.transactionreconciliator.transaction.upload;

import com.caponong.transactionreconciliator.ApplicationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.caponong.transactionreconciliator.constant.TransactionsConstant.FIRST_TRANSACTION_IDENTIFIER;
import static com.caponong.transactionreconciliator.constant.TransactionsConstant.SECOND_TRANSACTION_IDENTIFIER;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class TransactionUploadSuccessIT extends ApplicationTest {

    @Test
    public void uploadSuccessTest() throws Exception {
        mockMvc.perform(
                multipart(TRANSACTION_UPLOAD_API)
                        .file(getTestDataCsv("firstTransactionSet", "testDataCsvOne.csv"))
                        .file(getTestDataCsv("secondTransactionSet", "testDataCsvTwo.csv"))
                        .servletPath(TRANSACTION_UPLOAD_API)

        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("0")))
                .andExpect(jsonPath("$.message", is("success")))
                .andExpect(jsonPath("$.body.reconciliationToken").exists())
                .andExpect(jsonPath("$.body.tokenExpiry").exists())
                .andDo(print());
        
        wait(500);

        Assert.assertEquals(3, transactionRepository.getTotalUniqueTransactions(FIRST_TRANSACTION_IDENTIFIER + "%"));
        Assert.assertEquals(3, transactionRepository.getTotalUniqueTransactions(SECOND_TRANSACTION_IDENTIFIER + "%"));
    }
}
