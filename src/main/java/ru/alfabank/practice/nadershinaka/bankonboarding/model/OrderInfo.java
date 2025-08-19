package ru.alfabank.practice.nadershinaka.bankonboarding.model;

import java.util.List;

public class OrderInfo {

    //    объект с итоговой суммой и массивом товаров (название товара, цена за единицу, количество, сумма).
    private int amountOrder;
    private List<Order> orders;


    public OrderInfo(int amountOrder, List<Order> orders) {
        this.amountOrder = amountOrder;
        this.orders = orders;
    }

    public int getAmountOrder() {
        return amountOrder;
    }

    public void setAmountOrder(int amountOrder) {
        this.amountOrder = amountOrder;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
