package ru.alfabank.practice.nadershinaka.bankonboarding.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Discount;
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

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;


    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAllByAvailableTrue();
    }


    @Override
    public List<Product> getAllNotAvailableProduct() {
        return productRepository.findAllByAvailableFalse();
    }

    //ищем по id скидку, берем первую либо возвращаем 0
//    public Integer getDiscountByProductId(String id) {
//        Integer discount = discountRepository.findAll()
//                .stream()
//                .filter(d -> d.getProductList().contains(id)) //проверяем у каждой скидки есть ли в ней товар с таким id
//                .map(d -> d.getPercent())
//                .findFirst()
//                .orElse(0);
//        return discount;
//    }


    //ищет по id товар, если товара нет - ошибка
    @Override
    public Product getProduct(String id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.orElseThrow(() -> new NoSuchProductException(id));
    }

    //Расчет скидки по id продукта
    public Integer calculateDiscount(String productId) {

        //Получаем список скидок продукта по id
        List<Discount> discountList = discountRepository.findByProductId(productId);
        Integer summDiscountByProductId = 0;

        //Вычисляем сумму скидок для продукта, скидка не должна превышать 50%
        for (Discount d : discountList) {
            if (d.getProductList().contains(productId)) {
                summDiscountByProductId += d.getPercent();
            }
        }
        if (summDiscountByProductId > 50) {
            summDiscountByProductId = 50;
        }
        return summDiscountByProductId;
    }

    //Расчет стоимости продукта с учетом количества и скидки
    public int calculateCostWithDiscount(Product product, Integer quantity, Integer summDiscountByProductId, Integer price) {
        return price * quantity - (price * summDiscountByProductId / 100) * quantity;
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
            if (!product.isAvailable()) {
                continue;
            }
            Integer price = product.getPrice();

            //Получаем размер скидки по продукту
            Integer summDiscountByProductId = calculateDiscount(productId);
            Order order = new Order(element.getQuantity(), product);

            //Рассчитываем общую сумму заказа
            if (summDiscountByProductId == 0) {
                totalPrice = order.totalCost();
            } else {
                totalPrice += calculateCostWithDiscount(product, element.getQuantity(), summDiscountByProductId, price);
            }
            orders.add(order);
        }
        return new OrderInfo(totalPrice, orders);
    }

//    //каждую минуту делает недоступным два товара
//    @Scheduled(fixedRate = 60000)
//    public void changeAvailable() {
//        List<Product> products = productRepository.findAll();
//        if (products.isEmpty()) {
//            return;
//        }
//        products.forEach(p -> p.setAvailable(true));
//        for (int i = 0; i < 2; i++) {
//            int index = random.nextInt(products.size());
//            Product p = products.get(index);
//            p.setAvailable(false);
//            logger.info("Change available of product ID:" + p.getId());
//            products.remove(index);
//        }
//        productRepository.saveAll(products);
//    }
}
