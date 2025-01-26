package com.test.driveanddeliver.infrastructure.repository;

import com.test.driveanddeliver.infrastructure.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface JpaUserRepository extends ReactiveCrudRepository<UserEntity, Long> {

    Mono<UserEntity> findByLogin(String login);
}