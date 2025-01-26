package com.test.driveanddeliver.infrastructure.adapter;

import com.test.driveanddeliver.domain.model.User;
import com.test.driveanddeliver.domain.repository.UserRepository;
import com.test.driveanddeliver.infrastructure.entity.UserEntity;
import com.test.driveanddeliver.infrastructure.repository.JpaUserRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaRepository;

    public UserRepositoryAdapter(JpaUserRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Mono<User> findByLogin(String login) {
        return jpaRepository.findByLogin(login)
                .map(this::toDomain);
    }

    @Override
    public Mono<User> findById(Long userId) {
        return jpaRepository.findById(userId)
                .map(this::toDomain);
    }

    private User toDomain(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setLogin(entity.getLogin());
        user.setPassword(entity.getPassword());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setEmail(entity.getEmail());
        user.setActivated(entity.isActivated());
        user.setAddress(entity.getAddress());
        return user;
    }
}