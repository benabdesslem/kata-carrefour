package com.test.driveanddeliver.domain.model;


public class UserAuthority {

    private Long userId;

    private String authorityName;


    public UserAuthority() {
    }

    public UserAuthority(Long userId, String authorityName) {
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
