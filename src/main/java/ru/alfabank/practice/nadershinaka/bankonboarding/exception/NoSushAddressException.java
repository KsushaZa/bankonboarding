package ru.alfabank.practice.nadershinaka.bankonboarding.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class NoSushAddressException extends ApplicationException {
    public NoSushAddressException(String deliveryAddress) {
        super("Адрес доставки не найден: " + deliveryAddress, "NO_SUCH_PRODUCT", Map.of("missingdeliveryAddress", deliveryAddress), HttpStatus.BAD_REQUEST, null);
    }
}
