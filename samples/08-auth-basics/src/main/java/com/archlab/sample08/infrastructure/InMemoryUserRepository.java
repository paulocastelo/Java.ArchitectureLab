package com.archlab.sample08.infrastructure;

import com.archlab.sample08.application.UserRepository;
import com.archlab.sample08.domain.AppUser;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<String, AppUser> byEmail = new ConcurrentHashMap<>();
    private final Map<String, AppUser> byId = new ConcurrentHashMap<>();

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return Optional.ofNullable(byEmail.get(email.toLowerCase()));
    }

    @Override
    public Optional<AppUser> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public AppUser save(AppUser user) {
        byEmail.put(user.getEmail().toLowerCase(), user);
        byId.put(user.getId(), user);
        return user;
    }
}
