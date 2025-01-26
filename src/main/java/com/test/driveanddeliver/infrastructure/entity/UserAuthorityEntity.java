package com.test.driveanddeliver.infrastructure.entity;

import org.springframework.data.relational.core.mapping.Table;

@Table("user_authority")
public class UserAuthorityEntity {

    private Long userId;

    private String authorityName;


    public UserAuthorityEntity() {
    }

    public UserAuthorityEntity(Long userId, String authorityName) {
        this.userId = userId;
        this.authorityName = authorityName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }
}
