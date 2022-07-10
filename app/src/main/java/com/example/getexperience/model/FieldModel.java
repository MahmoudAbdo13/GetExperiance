package com.example.getexperience.model;

public class FieldModel {
    private String id, name;

    public FieldModel() {
    }

    public FieldModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}