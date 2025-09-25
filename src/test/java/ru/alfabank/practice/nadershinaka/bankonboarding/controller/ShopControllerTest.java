package ru.alfabank.practice.nadershinaka.bankonboarding.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Product;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderCalculationRequest;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderCalculationRequestList;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWireMock(port = 0)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public class ShopControllerTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");
    @Autowired
    ProductRepository repository;
    @Autowired(required = false)
    private WireMockServer wireMockServer;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper; // автоматически настроен Spring'ом
    @Value("${dadata.test.apikey}")
    private String dadataApikey;

    @BeforeEach
    void fillData() {
        repository.deleteAll();
        repository.save(new Product("1", "Nut", 230, true));
        repository.save(new Product("2", "Oatmeal", 100, true));
        repository.save(new Product("3", "Porridge mix", 450, true));
        for (Product p : repository.findAll()) {
            System.out.println(p.getName());
        }
        stubFor(WireMock.post("/suggestions/api/4_1/rs/suggest/address")
                .withHeader("Content-Type", containing("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                  {"suggestions":[{"value":"уфа ленина д 22 кв 3","data":{"fias_level":"8"}}]}
                                """)));
    }

    //проверка вызова сервиса calc, ответ 200
    //проверить наличие OrderInfo
    @Test
    void shouldReturnOrderInfo() throws Exception {
        OrderCalculationRequest request1 = new OrderCalculationRequest();
        request1.setId("3");
        request1.setQuantity(1);

        OrderCalculationRequest request2 = new OrderCalculationRequest();
        request2.setId("2");
        request2.setQuantity(1);

        List<OrderCalculationRequest> orderCalculationRequestList = new ArrayList<>();
        orderCalculationRequestList.add(request1);
        orderCalculationRequestList.add(request2);

        OrderCalculationRequestList requestList = new OrderCalculationRequestList(orderCalculationRequestList);
        requestList.setDeliveryAddress("уфа ленина д 22 кв 3");

        mockMvc.perform(post("/calc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestList)))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.amountOrder").exists())
                .andExpect(jsonPath("$.orders").isArray());
    }

    //При отсутствии продукта ошибка 400 с текстом "Нет продукта с id:.."
    @Test
    void shouldReturnNoSuchProductException() throws Exception {
        OrderCalculationRequest request1 = new OrderCalculationRequest();
        request1.setId("3");
        request1.setQuantity(1);

        OrderCalculationRequest request2 = new OrderCalculationRequest();
        request2.setId("2");
        request2.setQuantity(1);

        List<OrderCalculationRequest> orderCalculationRequestList = new ArrayList<>();
        orderCalculationRequestList.add(request1);
        orderCalculationRequestList.add(request2);

        OrderCalculationRequestList requestList = new OrderCalculationRequestList(orderCalculationRequestList);
        requestList.setDeliveryAddress("уфа ленина д 22 кв 3");

        repository.deleteAll();
        mockMvc.perform(post("/calc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestList)))
                .andDo(print()).andExpect(status().is(400))
                .andExpect(jsonPath("$.message").value("Нет продукта с id: 3"));
    }
}
