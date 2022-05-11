package com.caponong.transactionreconciliator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@Data
@EqualsAndHashCode (callSuper = true)
public class MultipartCsvFile extends Writable {

    public MultipartCsvFile(MultipartFile multipartFile, String identifier) {
        this.multipartFile = multipartFile;
        this.identifier = identifier;
    }

    private MultipartFile multipartFile;
}
