package ru.alfabank.practice.nadershinaka.bankonboarding.model;

import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Product;

public class Order {

    private int quantity;
    private Product product;


    public Order(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int totalCost() {
        return product.getPrice() * quantity;
    }

}
