package com.tranquility.models;

public abstract class BaseEntity {

    protected String id;

    public BaseEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract String toJson();

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [id=" + id + "]";
    }
}