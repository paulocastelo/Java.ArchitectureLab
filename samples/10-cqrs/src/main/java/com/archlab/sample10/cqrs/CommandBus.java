package com.archlab.sample10.cqrs;

public interface CommandBus {
    <C extends Command, R> R send(C command);
}
