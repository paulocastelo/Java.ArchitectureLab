package com.archlab.sample01.controllers;

import com.archlab.sample01.services.PrototypeGreetingService;
import com.archlab.sample01.services.RequestGreetingService;
import com.archlab.sample01.services.SingletonGreetingService;
import java.util.Map;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lifetime")
public class LifetimeController {
    private final ObjectProvider<PrototypeGreetingService> prototypeProvider;
    private final SingletonGreetingService singleton1;
    private final SingletonGreetingService singleton2;
    private final RequestGreetingService request1;
    private final RequestGreetingService request2;

    public LifetimeController(
            ObjectProvider<PrototypeGreetingService> prototypeProvider,
            SingletonGreetingService singleton1,
            SingletonGreetingService singleton2,
            RequestGreetingService request1,
            RequestGreetingService request2) {
        this.prototypeProvider = prototypeProvider;
        this.singleton1 = singleton1;
        this.singleton2 = singleton2;
        this.request1 = request1;
        this.request2 = request2;
    }

    @GetMapping
    public Map<String, String> show() {
        var prototype1 = prototypeProvider.getObject();
        var prototype2 = prototypeProvider.getObject();

        return Map.of(
                "prototype1", prototype1.greet(),
                "prototype2", prototype2.greet(),
                "singleton1", singleton1.greet(),
                "singleton2", singleton2.greet(),
                "request1", request1.greet(),
                "request2", request2.greet());
    }
}
