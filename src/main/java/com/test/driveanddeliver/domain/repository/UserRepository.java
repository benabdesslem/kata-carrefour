package com.test.driveanddeliver.domain.repository;

import com.test.driveanddeliver.domain.model.User;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> findByLogin(String login);

    Mono<User> findById(Long userId);
}