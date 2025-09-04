package ru.alfabank.practice.nadershinaka.bankonboarding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Discount;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderCalculationRequestList;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderInfo;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Product;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.ProductList;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.ProductRepository;
import ru.alfabank.practice.nadershinaka.bankonboarding.service.ProductService;


import java.util.List;

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
    }//todo вернуть объект json



    //на входе массив объектов (id товара и количество)
    //возвращает объект, который содержит сумму заказа с учетом скидки и массив товаров(данные о продукте, количество, сумма без скидки)
    //скидка суммируется, но не более 50%
    //если вводишь id несуществующего товара - ошибка 400 с id товара
    @PostMapping(value = "/calc", produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderInfo calcOrder(@RequestBody OrderCalculationRequestList orderCalculationRequestList) {
        OrderInfo orderInfo = productService.calculateOrder(orderCalculationRequestList.getOrderCalculationRequestList());
        return orderInfo;
    }

//    @GetMapping("/product/{id}/{name}")
//    public List<Product> getProductsByIdAndName(@PathVariable String id, @PathVariable String name) {
//       return productService.findAllByIdAndName(id, name);
//    }


//    @GetMapping("/product/{id}")
//    public Product getProductById(@PathVariable String id) {
//        Product product = productService.getProduct(id);
//        return product;
//    }

    @PostMapping(value = "/saveProduct", produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

//    @PostMapping(value = "/saveDiscount", produces = MediaType.APPLICATION_JSON_VALUE)
//    public void saveDiscount(Discount discount) {
//        productService.saveDiscount(discount);
//    }
}


