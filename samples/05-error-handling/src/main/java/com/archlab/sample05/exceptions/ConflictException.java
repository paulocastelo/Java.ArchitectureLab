package com.archlab.sample05.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException(String detail) {
        super(detail);
    }
}
