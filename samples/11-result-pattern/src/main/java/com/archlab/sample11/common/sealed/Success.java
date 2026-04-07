package com.archlab.sample11.common.sealed;

public record Success<T>(T value) implements ResultBase<T> {
}
