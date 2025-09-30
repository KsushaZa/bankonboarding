package ru.alfabank.practice.nadershinaka.bankonboarding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Product;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderCalculationRequestList;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderInfo;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.ProductRepository;
import ru.alfabank.practice.nadershinaka.bankonboarding.service.ProductService;

@RestController
@RequestMapping
public class ShopController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;


    @GetMapping("/welcome")
    public String getWelcomeWord() {
        return "Добро пожаловать в наш чудесный магазин!";
    }

    @PostMapping(value = "/saveProduct", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @PostMapping(value = "/calc", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderInfo calcOrder(@RequestBody OrderCalculationRequestList orderCalculationRequestList) {
        OrderInfo orderInfo = productService.calculateOrder(orderCalculationRequestList);
        return orderInfo;
    }
}


