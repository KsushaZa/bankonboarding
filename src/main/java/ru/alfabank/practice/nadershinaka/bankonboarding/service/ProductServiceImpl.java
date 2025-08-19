package ru.alfabank.practice.nadershinaka.bankonboarding.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Discount;
import ru.alfabank.practice.nadershinaka.bankonboarding.exeption.ApplicationException;
import ru.alfabank.practice.nadershinaka.bankonboarding.exeption.NoSuchProductException;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.Order;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderCalculationRequest;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderInfo;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Product;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.DiscountRepository;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.ProductRepository;

import java.util.*;


@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;


    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAllByAvailableIsTrue();
    }


    @Override
    public List<Product> getAllNotAvailableProduct() {
        return productRepository.findAllByAvailableIsFalse();
    }

    //ищем по id скидку, берем первую либо возвращаем 0
    public Integer getDiscountByProductId(String id) {
        Integer discount = discountRepository.findAll()
                .stream()
                .filter(d -> d.getProductList().contains(id)) //проверяем у каждой скидки есть ли в ней товар с таким id
                .map(d -> d.getPercent())
                .findFirst()
                .orElse(0);
        return discount;
    }


    //ищет по id товар, если товара нет - ошибка
    @Override
    public Product getProduct(String id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.orElseThrow(() -> new NoSuchProductException(id));
    }

    //на входе массив объектов (id товара и количество)
    //возвращает объект, который содержит сумму заказа с учетом скидки и массив товаров(данные о продукте, количество, сумма без скидки)
    //скидка суммируется, но не более 50%
    //если вводишь id несуществующего товара - ошибка 400 с id товара
    @Override
    public OrderInfo calculateOrder(List<OrderCalculationRequest> orderCalculationRequests) {
        int totalPrice = 0;
        List<Order> orders = new ArrayList<>();

        for (OrderCalculationRequest element : orderCalculationRequests) {
            String productId = element.getId().toString();
            Product product = getProduct(productId);
            Integer price = product.getPrice();
            Integer summDiscountByProductId = 0;

            //получили список скидок по id
            List<Discount> discountList = discountRepository.findByProductId(productId);
            //сложили все скидки для одного продукта
            for(Discount d:discountList) {
             if (d.getProductList().contains(productId)) {
               summDiscountByProductId += d.getPercent();
             }
            }

            if (summDiscountByProductId > 50) {
                summDiscountByProductId = 50;
            }

            if (!product.isAvailable()) {
                continue;
            }
            Order order = new Order(element.getQuantity(), product);

            if (summDiscountByProductId == 0) {
                continue;
            }
            //расчет суммы для конкретного продукта с учетом скидки и количества
            Integer totalCostWithDiscount = order.totalCost() - (price * summDiscountByProductId / 100) * element.getQuantity();
            //расчет общей суммы заказа
            totalPrice += totalCostWithDiscount;
            orders.add(order);
        }
        return new OrderInfo(totalPrice, orders);
    }


    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    //каждую минуту делает недоступным два товара
    @Scheduled(fixedRate = 60000)
    public void changeAvailable() {
        List<Product> productListNotAvailable = getAllNotAvailableProduct()
                .stream()
                .peek(p -> p.setAvailable(true))
                .toList();
        productRepository.saveAll(productListNotAvailable);
        List<Product> productListAvailable = getProducts();
        for (int i = 0; i < 2; i++) {
            Random random = new Random();
            int index = random.nextInt(productListAvailable.size()); //todo проверка на пустоту
            Product p = productListAvailable.get(index);
            p.setAvailable(false);
            saveProduct(p);
            logger.info("Change available of product ID:" + p.getId());
//            productListAvailable.remove(index);
        }
    }

//    @Override
//    public void saveDiscount(Discount discount) {
//        discountRepository.save(discount);
//    }
}
