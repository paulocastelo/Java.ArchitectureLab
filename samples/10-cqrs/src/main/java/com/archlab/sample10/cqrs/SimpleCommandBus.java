package com.archlab.sample10.cqrs;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

@Component
public class SimpleCommandBus implements CommandBus {
    private final Map<Class<?>, CommandHandler<?, ?>> handlers;

    public SimpleCommandBus(List<CommandHandler<?, ?>> handlerList) {
        this.handlers = handlerList.stream().collect(Collectors.toMap(this::resolveCommandType, Function.identity()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Command, R> R send(C command) {
        var handler = (CommandHandler<C, R>) handlers.get(command.getClass());
        if (handler == null) {
            throw new IllegalArgumentException("No command handler found for " + command.getClass().getSimpleName());
        }
        return handler.handle(command);
    }

    private Class<?> resolveCommandType(CommandHandler<?, ?> handler) {
        return ResolvableType.forClass(handler.getClass()).as(CommandHandler.class).resolveGeneric(0);
    }
}
