package com.chaimm.rcmd.entity;

import java.io.Serializable;
import java.util.Random;

/**
 * @author 大闲人柴毛毛
 * @date 2018/2/6 下午9:30
 * @description
 */
public class Category implements Serializable {
    private String id;
    private String name;
    private Category parent;

    public Category(String id, String name, Category parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
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

    public void setName(String name) {
        this.name = name;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parent=" + parent +
                '}';
    }
}
