package ru.alfabank.practice.nadershinaka.bankonboarding.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Product;
import ru.alfabank.practice.nadershinaka.bankonboarding.exeption.NoSuchProductException;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderCalculationRequest;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderCalculationRequestList;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

//    @BeforeEach
//    void fillData(@Autowired ProductRepository repository) {
//        repository.deleteAll();
//        repository.save(new Product("66", "Nut", 230, true));
//        repository.save(new Product("67", "Oatmeal", 100, true));
//        repository.save(new Product("68", "Porridge mix", 450, true));
//        for (Product p : repository.findAll()) {
//            System.out.println(p.getName());
//        }
//    }

    @Autowired
    private ObjectMapper objectMapper; // автоматически настроен Spring'ом


    @Autowired
    ProductRepository repository;

    @BeforeEach
    void fillData(@Autowired ProductRepository repository) {
        repository.deleteAll();
        repository.save(new Product("1", "Nut", 230, true));
        repository.save(new Product("2", "Oatmeal", 100, true));
        repository.save(new Product("3", "Porridge mix", 450, true));
        for (Product p : repository.findAll()) {
            System.out.println(p.getName());

        }
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

        mockMvc.perform(post("/calc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestList)))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.amountOrder").exists())
                .andExpect(jsonPath("$.orders").isArray());
    }
//
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

        repository.deleteAll();
        mockMvc.perform(post("/calc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestList)))
                .andExpect(jsonPath("$.").value("Нет продукта с id: 3"));
//                .andDo(print()).andExpect(status().isInternalServerError()); // 500
//                .andExpect(jsonPath("$.message").value("Нет продукта с id: 3"));
//                .andExpect(status().isBadRequest());
//                .andExpect(jsonPath("$.message").value("Нет продукта с id"));
    }

    //проверка методов в сервисе:
    //пустой реквест
    //реквест с несущ-м продуктом
    //применилась ли скидка (отдельно проверить метод со скидкой)
    //через моки без заполнения бд
}
