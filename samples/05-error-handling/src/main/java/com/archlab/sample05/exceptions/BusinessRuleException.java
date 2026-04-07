package com.archlab.sample05.exceptions;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String detail) {
        super(detail);
    }
}
