package com.test.driveanddeliver.infrastructure.repository;

import com.test.driveanddeliver.infrastructure.entity.UserAuthorityEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface JpaUserAuthorityRepository extends ReactiveCrudRepository<UserAuthorityEntity, Long> {
    Flux<UserAuthorityEntity> findByUserId(Long userId);
}
