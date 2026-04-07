package com.archlab.sample11.common.sealed;

public sealed interface ResultBase<T> permits Success, Failure {
}
