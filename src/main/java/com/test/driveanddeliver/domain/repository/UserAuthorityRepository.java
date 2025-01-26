package com.test.driveanddeliver.domain.repository;

import com.test.driveanddeliver.domain.model.UserAuthority;
import reactor.core.publisher.Flux;

public interface UserAuthorityRepository {
    Flux<UserAuthority> findByUserId(Long userId);
}
