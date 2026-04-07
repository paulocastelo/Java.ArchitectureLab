package com.archlab.sample11.common.sealed;

public record Failure<T>(String error, String errorCode) implements ResultBase<T> {
}
