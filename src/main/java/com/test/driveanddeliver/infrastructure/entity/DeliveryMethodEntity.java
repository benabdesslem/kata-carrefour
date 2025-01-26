package com.test.driveanddeliver.infrastructure.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("delivery_method")
public class DeliveryMethodEntity {
    @Id
    private Long id;
    private String name;

    public DeliveryMethodEntity() {
    }

    public DeliveryMethodEntity(String name) {
        this.name = name;
    }

    public DeliveryMethodEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}