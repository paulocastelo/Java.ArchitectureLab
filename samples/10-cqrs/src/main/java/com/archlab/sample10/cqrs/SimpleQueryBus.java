package com.archlab.sample10.cqrs;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

@Component
public class SimpleQueryBus implements QueryBus {
    private final Map<Class<?>, QueryHandler<?, ?>> handlers;

    public SimpleQueryBus(List<QueryHandler<?, ?>> handlerList) {
        this.handlers = handlerList.stream().collect(Collectors.toMap(this::resolveQueryType, Function.identity()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Q extends Query<R>, R> R send(Q query) {
        var handler = (QueryHandler<Q, R>) handlers.get(query.getClass());
        if (handler == null) {
            throw new IllegalArgumentException("No query handler found for " + query.getClass().getSimpleName());
        }
        return handler.handle(query);
    }

    private Class<?> resolveQueryType(QueryHandler<?, ?> handler) {
        return ResolvableType.forClass(handler.getClass()).as(QueryHandler.class).resolveGeneric(0);
    }
}
