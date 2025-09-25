package ru.alfabank.practice.nadershinaka.bankonboarding.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alfabank.practice.nadershinaka.bankonboarding.dadataClient.DaDataClient;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Discount;
import ru.alfabank.practice.nadershinaka.bankonboarding.exeption.NoSuchProductException;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderCalculationRequest;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderCalculationRequestList;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.dto.DadataResponse;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.dto.Data;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.dto.Suggestions;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.DiscountRepository;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.ProductRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
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

        int result = productService.calculateCostWithDiscount(quantity, discountPercent, price);

        assertEquals(270, result);
    }

    @Test
    public void calculateCostWithDiscount_shouldNotThrowIfQuantity0() {
        Integer price = 100;
        Integer quantity = 0;
        Integer discountPercent = 10;

        Assertions.assertDoesNotThrow(() -> productService.calculateCostWithDiscount(quantity, discountPercent, price));
    }


    @Test
    public void calculateOrder_shouldReturnTotalPrice() {
        OrderCalculationRequest or1 = new OrderCalculationRequest("1", 2);
        OrderCalculationRequest or2 = new OrderCalculationRequest("3", 1);

        List<OrderCalculationRequest> orderCalculationRequests = Arrays.asList(or1, or2);
        OrderCalculationRequestList orderCalculationRequestList = new OrderCalculationRequestList(orderCalculationRequests);
        orderCalculationRequestList.setDeliveryAddress("жопа");

        Data data = new Data();
        data.setFias_level("8");

        Suggestions suggestion = new Suggestions();
        suggestion.setData(data);

        DadataResponse response = new DadataResponse();
        response.setSuggestions(Arrays.asList(suggestion)); // безопасно

        when(daDataClient.searchAddress(anyString(), anyMap())).thenReturn(response);
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchProductException.class, () -> productService.calculateOrder(orderCalculationRequestList));
    }
//    @BeforeEach
//    public void setup() {
//        this.productService.setBase("http://localhost:"
//                + this.environment.getProperty("wiremock.server.port"));
//    }
//
//    // Using the WireMock APIs in the normal way:
//    @Test
//    public void contextLoads() throws Exception {
//        // Stubbing WireMock
//        stubFor(get(urlEqualTo("/resource")).willReturn(aResponse()
//                .withHeader("Content-Type", "text/plain").withBody("Hello World!")));
//        // We're asserting if WireMock responded properly
//        assertThat(this.service.go()).isEq

}
