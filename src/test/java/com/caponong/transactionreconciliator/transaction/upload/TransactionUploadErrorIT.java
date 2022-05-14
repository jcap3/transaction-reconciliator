package com.caponong.transactionreconciliator.transaction.upload;

import com.caponong.transactionreconciliator.ApplicationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;

import static com.caponong.transactionreconciliator.constant.TransactionsConstant.FIRST_TRANSACTION_IDENTIFIER;
import static com.caponong.transactionreconciliator.constant.TransactionsConstant.SECOND_TRANSACTION_IDENTIFIER;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TransactionUploadErrorIT extends ApplicationTest {

    @Test
    public void testMissingRequestPart() throws Exception {
        mockMvc.perform(
                multipart(TRANSACTION_UPLOAD_API)
                        .file(getTestDataCsv("firstTransactionSets", "testDataCsvOne.csv"))
                        .file(getTestDataCsv("secondTransactionSet", "testDataCsvTwo.csv"))
                        .servletPath(TRANSACTION_UPLOAD_API)

        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("999")))
                .andExpect(jsonPath("$.message", is("error")))
                .andExpect(jsonPath("$.errorCode.code", is("001")))
                .andExpect(jsonPath("$.errorCode.message", is("Missing request part")))
                .andDo(print());

    }

    @Test
    public void testInvalidFileUploaded() throws Exception {
        mockMvc.perform(
                multipart(TRANSACTION_UPLOAD_API)
                        .file(getTestDataCsv("firstTransactionSet", "testDataCsvOne.csv"))
                        .file(new MockMultipartFile("secondTransactionSet", "testFile.png", 
                                "text/csv", new byte[]{}))
                        .servletPath(TRANSACTION_UPLOAD_API)

        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("999")))
                .andExpect(jsonPath("$.message", is("error")))
                .andExpect(jsonPath("$.errorCode.code", is("002")))
                .andExpect(jsonPath("$.errorCode.message", is("File must be in .csv")))
                .andDo(print());
    }
}
