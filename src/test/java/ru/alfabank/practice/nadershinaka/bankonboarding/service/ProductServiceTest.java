package ru.alfabank.practice.nadershinaka.bankonboarding.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Discount;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Product;
import ru.alfabank.practice.nadershinaka.bankonboarding.exeption.NoSuchProductException;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderCalculationRequest;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.DiscountRepository;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.ProductRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private DiscountRepository discountRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private static Discount d1, d2, d3;

    @BeforeAll
    static void setUp() {
        d1 = new Discount("1", "family", Arrays.asList("1", "2"), 50, true, LocalDate.now());
        d2 = new Discount("2", "social", Arrays.asList("3", "4"), 10, true, LocalDate.now());
        d3 = new Discount("3", "child", Arrays.asList("1", "5"), 10, true, LocalDate.now());
    }

    @Test
    public void calculateDiscount_shouldDiscountEqual50() {
        List<Discount> discounts = Arrays.asList(d1, d3);
        when(discountRepository.findByProductId("1")).thenReturn(discounts);

        Integer result = productService.calculateDiscount("1");
        assertEquals(50, result);
    }

    @Test
    public void calculateDiscount_shouldDiscountLess50() {
        List<Discount> discounts = Arrays.asList(d2, d3);

        when(discountRepository.findByProductId("1")).thenReturn(discounts);

        Integer result = productService.calculateDiscount("1");
        Assertions.assertTrue(result < 50);
    }

    @Test
    public void calculateCostWithDiscount_shouldReturnCostWithDiscount() {
        Integer price = 100;
        Integer quantity = 3;
        Integer discountPercent = 10;

        int result = productService.calculateCostWithDiscount( quantity, discountPercent, price);

        assertEquals(270, result);
    }

    @Test
    public void calculateCostWithDiscount_shouldNotThrowIfQuantity0() {
        Integer price = 100;
        Integer quantity = 0;
        Integer discountPercent = 10;

        Assertions.assertDoesNotThrow(()->productService.calculateCostWithDiscount( quantity, discountPercent, price));
    }


    @Test
    public void calculateOrder_shouldReturnTotalPrice() {
        OrderCalculationRequest or1 = new OrderCalculationRequest("1", 2);
        OrderCalculationRequest or2 = new OrderCalculationRequest("3", 1);
        OrderCalculationRequest or3 = new OrderCalculationRequest("14", 1);

        List<OrderCalculationRequest> orderCalculationRequests = Arrays.asList(or1, or2, or3);

        when(productRepository.findById("1")).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchProductException.class, () -> productService.calculateOrder(orderCalculationRequests));
    }
}
