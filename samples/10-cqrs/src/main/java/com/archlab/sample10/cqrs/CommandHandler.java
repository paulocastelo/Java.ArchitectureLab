package com.archlab.sample10.cqrs;

public interface CommandHandler<C extends Command, R> {
    R handle(C command);
}
