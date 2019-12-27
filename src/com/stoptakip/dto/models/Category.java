package com.stoptakip.dto.models;

public class Category {

    private int id;
    private String name;
    private int parent_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public int getParent_id() { return parent_id; }

    public void setParent_id(int parentId) { this.parent_id = parentId; }

    @Override
    public String toString() {
        return name;
    }
}
