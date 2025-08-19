package ru.alfabank.practice.nadershinaka.bankonboarding.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// todo создать entity
@Document("products")
public class Product {

    @Id
    private String id;
    private String name;
    private Integer price;
    private Boolean available;

    public Product() {
    }

    public Product(String id, String name, Integer price, Boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = available;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
