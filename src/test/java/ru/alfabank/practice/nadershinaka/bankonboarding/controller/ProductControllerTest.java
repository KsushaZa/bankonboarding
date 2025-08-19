package ru.alfabank.practice.nadershinaka.bankonboarding.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Product;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.ProductRepository;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @BeforeEach
    void fillData(@Autowired ProductRepository repository) {
        repository.deleteAll();
        repository.save(new Product("66", "Nut", 230, true));
        repository.save(new Product("67", "Oatmeal", 100, true));
        repository.save(new Product("68", "Porridge mix", 450, true));
        for (Product p : repository.findAll()) {
            System.out.println(p.getName());
        }
    }

    //убедиться что ответ 200
    //сравнить кол-во продуктов в репозитории и в ответе
    @Test
    void shouldReturnProducts(@Autowired ProductRepository repository) throws Exception {  //контроллер-сервис-репозиторий-подключение к бд(докер)
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(jsonPath("$.productList.length()").value(repository.count()));
    }

    //почистить докер и проверить что в ответе пустой массив
    @Test
    void shouldReturnEmptyProducts(@Autowired ProductRepository repository) throws Exception {
        repository.deleteAll();
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andDo(print())
                .andExpect(jsonPath("$.productList").isEmpty());
    }
}