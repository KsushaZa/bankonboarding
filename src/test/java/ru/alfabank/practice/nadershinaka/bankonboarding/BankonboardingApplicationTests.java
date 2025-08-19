package ru.alfabank.practice.nadershinaka.bankonboarding;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.alfabank.practice.nadershinaka.bankonboarding.controller.ShopController;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.ProductList;


import java.util.List;

@SpringBootTest
class BankonboardingApplicationTests {
	private static final Logger log = LoggerFactory.getLogger(BankonboardingApplicationTests.class);
	@Autowired
	private ShopController shopController;


	@Test
	void contextLoads() {
	}

	@Test
	void expectWelcomeMessage() {
		String answer = shopController.getWelcomeWord();
		Assertions.assertEquals("Добро пожаловать в наш чудесный магазин!", answer);
	}

//	@Test
//	void expectProductList () {
//		ProductList productList = shopController.getProduct();
//		Assertions.assertEquals(productList, productList);
//
//	}

}
