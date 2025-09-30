package ru.alfabank.practice.nadershinaka.bankonboarding.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Product;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.ProductResponse;
import ru.alfabank.practice.nadershinaka.bankonboarding.service.ProductService;

import java.util.List;


@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    //Возвращает список всех доступных продуктов
    @GetMapping("/products")
    public ProductResponse getProducts() {
        List<Product> products = productService.getProducts();
        return new ProductResponse(products);
    }


}
