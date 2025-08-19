package ru.alfabank.practice.nadershinaka.bankonboarding.model;

import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Product;
import java.util.List;

public class ProductList {
    private List<Product> productList;

    public ProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
