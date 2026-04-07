package com.archlab.sample10.cqrs;

public interface QueryHandler<Q extends Query<R>, R> {
    R handle(Q query);
}
