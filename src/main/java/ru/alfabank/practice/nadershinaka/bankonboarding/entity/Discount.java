package ru.alfabank.practice.nadershinaka.bankonboarding.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document("discounts")
public class Discount {

    @Id
    private String id;
    private String name;
    private List<String> productList;
    private Integer percent;
    private Boolean available;
    private LocalDate lastUpdate;

    public Discount(String id, String name, List<String> productList, Integer percent, Boolean available, LocalDate lastUpdate) {
        this.id = id;
        this.name = name;
        this.productList = productList;
        this.percent = percent;
        this.available = available;
        this.lastUpdate = lastUpdate;
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

    public List<String> getProductList() {
        return productList;
    }

    public void setProductList(List<String> productList) {
        this.productList = productList;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
