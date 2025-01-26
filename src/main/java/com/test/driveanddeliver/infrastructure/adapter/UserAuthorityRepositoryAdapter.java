package com.test.driveanddeliver.infrastructure.adapter;

import com.test.driveanddeliver.domain.model.UserAuthority;
import com.test.driveanddeliver.domain.repository.UserAuthorityRepository;
import com.test.driveanddeliver.infrastructure.entity.UserAuthorityEntity;
import com.test.driveanddeliver.infrastructure.repository.JpaUserAuthorityRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class UserAuthorityRepositoryAdapter implements UserAuthorityRepository {

    private final JpaUserAuthorityRepository jpaRepository;

    public UserAuthorityRepositoryAdapter(JpaUserAuthorityRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Flux<UserAuthority> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId)
                .map(this::toDomain);
    }

    private UserAuthority toDomain(UserAuthorityEntity entity) {
        return new UserAuthority( entity.getUserId(), entity.getAuthorityName());
    }
}