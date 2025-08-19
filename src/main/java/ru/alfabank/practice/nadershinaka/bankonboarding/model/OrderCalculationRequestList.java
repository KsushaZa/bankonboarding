package ru.alfabank.practice.nadershinaka.bankonboarding.model;

import java.util.List;

public class OrderCalculationRequestList {
    private List<OrderCalculationRequest> orderCalculationRequestList;

    public OrderCalculationRequestList(List<OrderCalculationRequest> orderCalculationRequestList) {
        this.orderCalculationRequestList = orderCalculationRequestList;
    }

    public List<OrderCalculationRequest> getOrderCalculationRequestList() {
        return orderCalculationRequestList;
    }

    public void setOrderCalculationRequestList(List<OrderCalculationRequest> orderCalculationRequestList) {
        this.orderCalculationRequestList = orderCalculationRequestList;
    }
}
