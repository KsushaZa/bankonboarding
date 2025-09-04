package ru.alfabank.practice.nadershinaka.bankonboarding.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Product;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.ProductRepository;

import java.util.List;
import java.util.Random;

@Component
public class SchedulerAvailableProduct {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerAvailableProduct.class);

    private final ProductRepository productRepository;
    private final Random random;

    public SchedulerAvailableProduct(ProductRepository productRepository, Random random) {
        this.productRepository = productRepository;
        this.random = random;
    }

    //каждую минуту делает недоступным два товара
    @Scheduled(fixedRate = 60000)
    public void changeAvailable() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            return;
        }
        products.forEach(p -> p.setAvailable(true));
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(products.size());
            Product p = products.get(index);
            p.setAvailable(false);
            logger.info("Change available of product ID:" + p.getId());
        }
        productRepository.saveAll(products);
    }
}
