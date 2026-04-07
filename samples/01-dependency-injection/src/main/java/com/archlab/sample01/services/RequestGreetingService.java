package com.archlab.sample01.services;

import java.util.UUID;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.stereotype.Component;

@Component
@RequestScope
public class RequestGreetingService implements IGreetingService {
    private final String instanceId = UUID.randomUUID().toString();

    @Override
    public String greet() {
        return "Request: " + instanceId;
    }
}
