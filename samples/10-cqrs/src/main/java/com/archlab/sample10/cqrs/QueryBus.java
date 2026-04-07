package com.archlab.sample10.cqrs;

public interface QueryBus {
    <Q extends Query<R>, R> R send(Q query);
}
