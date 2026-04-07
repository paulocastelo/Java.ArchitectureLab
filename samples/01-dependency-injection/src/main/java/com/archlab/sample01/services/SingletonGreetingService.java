package com.archlab.sample01.services;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class SingletonGreetingService implements IGreetingService {
    private final String instanceId = UUID.randomUUID().toString();

    @Override
    public String greet() {
        return "Singleton: " + instanceId;
    }
}
