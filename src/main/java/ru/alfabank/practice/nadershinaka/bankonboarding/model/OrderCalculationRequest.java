package ru.alfabank.practice.nadershinaka.bankonboarding.model;

public class OrderCalculationRequest {

    //    массив объектов (идентификатор товара, количество)


    private String id;
    private int quantity;


    public OrderCalculationRequest() {
    }

    public OrderCalculationRequest(String id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
