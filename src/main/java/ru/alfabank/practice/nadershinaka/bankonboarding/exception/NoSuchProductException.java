package ru.alfabank.practice.nadershinaka.bankonboarding.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class NoSuchProductException extends ApplicationException {

    public NoSuchProductException(String productId) {
        super("Нет продукта с id: " + productId, "NO_SUCH_PRODUCT", Map.of("missingProductId", productId), HttpStatus.BAD_REQUEST, null);
    }
}
