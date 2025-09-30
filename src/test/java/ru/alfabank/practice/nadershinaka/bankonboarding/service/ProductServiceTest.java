package ru.alfabank.practice.nadershinaka.bankonboarding.service;


import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import ru.alfabank.practice.nadershinaka.bankonboarding.client.DaDataClient;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Discount;
import ru.alfabank.practice.nadershinaka.bankonboarding.exception.NoSuchProductException;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderCalculationRequest;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderCalculationRequestList;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.dto.DadataRequest;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.dto.DadataResponse;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.dto.Data;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.dto.Suggestions;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.DiscountRepository;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.ProductRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    private static Discount d1, d2, d3;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private DiscountRepository discountRepository;
    @Mock
    private DaDataClient daDataClient;
    @InjectMocks
    private ProductServiceImpl productService;


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

        int expectedDiscountedPrice = productService.calculateCostWithDiscount(quantity, discountPercent, price);

        assertEquals(270, expectedDiscountedPrice);
    }

    @Test
    public void calculateCostWithDiscount_shouldNotThrowIfQuantity0() {
        Integer price = 100;
        Integer quantity = 0;
        Integer discountPercent = 10;

        Assertions.assertDoesNotThrow(() -> productService.calculateCostWithDiscount(quantity, discountPercent, price));
    }

    @Test
    void calculateOrder_shouldThrowNoSuchProductException() {
        OrderCalculationRequest or1 = new OrderCalculationRequest("1", 2);
        OrderCalculationRequestList requestList = new OrderCalculationRequestList(List.of(or1));
        requestList.setDeliveryAddress("уфа ленина д 3 кв 2");

        Data data = new Data();
        data.setFias_level("8");
        Suggestions suggestion = new Suggestions();
        suggestion.setData(data);
        DadataResponse response = new DadataResponse();
        response.setSuggestions(List.of(suggestion));
        when(daDataClient.searchAddress(ArgumentMatchers.any(DadataRequest.class)))
                .thenReturn(response);
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(NoSuchProductException.class,
                () -> productService.calculateOrder(requestList));
    }
}

