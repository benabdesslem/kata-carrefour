package com.test.driveanddeliver.infrastructure.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("authority")
public class AuthorityEntity {

    @Id
    private String name;
}
