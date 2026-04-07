package com.archlab.sample01.services;

import java.util.UUID;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PrototypeGreetingService implements IGreetingService {
    private final String instanceId = UUID.randomUUID().toString();

    @Override
    public String greet() {
        return "Prototype: " + instanceId;
    }
}
