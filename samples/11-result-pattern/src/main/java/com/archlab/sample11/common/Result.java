package com.archlab.sample11.common;

public final class Result<T> {
    private final boolean success;
    private final T value;
    private final String error;
    private final String errorCode;

    private Result(T value) {
        this.success = true;
        this.value = value;
        this.error = null;
        this.errorCode = null;
    }

    private Result(String error, String errorCode) {
        this.success = false;
        this.value = null;
        this.error = error;
        this.errorCode = errorCode;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value);
    }

    public static <T> Result<T> failure(String error, String errorCode) {
        return new Result<>(error, errorCode);
    }

    public boolean isSuccess() { return success; }
    public T getValue() { return value; }
    public String getError() { return error; }
    public String getErrorCode() { return errorCode; }
}
