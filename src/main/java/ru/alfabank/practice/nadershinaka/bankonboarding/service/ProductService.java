package ru.alfabank.practice.nadershinaka.bankonboarding.service;

import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderCalculationRequest;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderInfo;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Product;

import java.util.List;

public interface ProductService {

    public List<Product> getProducts();
    List<Product> getAllNotAvailableProduct();
    public Product getProduct(String id);
    OrderInfo calculateOrder(List<OrderCalculationRequest> orderCalculationRequests);

}
