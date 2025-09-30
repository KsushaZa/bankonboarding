package ru.alfabank.practice.nadershinaka.bankonboarding.model;

import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Product;
import java.util.List;

public class ProductResponse {
    private List<Product> productList;

    public ProductResponse(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
