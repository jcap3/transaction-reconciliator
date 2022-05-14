package com.caponong.transactionreconciliator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;

@Data
@EqualsAndHashCode (callSuper = true)
public class CsvFile extends Writable {

    public CsvFile(File file, String identifier) {
        this.file = file;
        this.identifier = identifier;
    }

    private File file;
}
