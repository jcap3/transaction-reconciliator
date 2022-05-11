package com.caponong.transactionreconciliator.services;

import com.caponong.transactionreconciliator.model.Writable;

public interface Writer<T extends Writable> {

    void write(T data);
}
