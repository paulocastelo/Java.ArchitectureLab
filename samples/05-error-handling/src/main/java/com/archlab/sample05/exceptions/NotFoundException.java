package com.archlab.sample05.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String detail) {
        super(detail);
    }
}
