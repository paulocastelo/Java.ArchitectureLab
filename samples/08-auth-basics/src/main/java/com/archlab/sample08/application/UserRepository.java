package com.archlab.sample08.application;

import com.archlab.sample08.domain.AppUser;
import java.util.Optional;

public interface UserRepository {
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findById(String id);
    AppUser save(AppUser user);
}
