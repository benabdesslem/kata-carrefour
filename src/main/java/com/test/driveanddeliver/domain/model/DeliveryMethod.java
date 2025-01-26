package com.test.driveanddeliver.domain.model;


public class DeliveryMethod {
    private Long id;
    private String name;

    public DeliveryMethod() {
    }

    public DeliveryMethod(String name) {
        this.name = name;
    }

    public DeliveryMethod(Long id, String name) {
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