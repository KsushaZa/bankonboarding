package ru.alfabank.practice.nadershinaka.bankonboarding.service;

import ru.alfabank.practice.nadershinaka.bankonboarding.logging.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.alfabank.practice.nadershinaka.bankonboarding.dadataClient.DaDataClient;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Discount;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Product;
import ru.alfabank.practice.nadershinaka.bankonboarding.exeption.NoSuchProductException;
import ru.alfabank.practice.nadershinaka.bankonboarding.exeption.NoSushAddressException;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.Order;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderCalculationRequest;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderCalculationRequestList;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.OrderInfo;
import ru.alfabank.practice.nadershinaka.bankonboarding.model.dto.DadataResponse;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.DiscountRepository;
import ru.alfabank.practice.nadershinaka.bankonboarding.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final DaDataClient daDataClient; // Feign-клиент внедрён

    public ProductServiceImpl(ProductRepository productRepository, DiscountRepository discountRepository, DaDataClient daDataClient) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        this.daDataClient = daDataClient;
    }

    @Value("${dadata.api-key}")
    private String daDataApiKey;

    @Log
    @Override
    public List<Product> getProducts() {
        return productRepository.findAllByAvailableTrue();
    }

    @Override
    public List<Product> getAllNotAvailableProduct() {
        return productRepository.findAllByAvailableFalse();
    }

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
    public int calculateCostWithDiscount(Integer quantity, Integer summDiscountByProductId, Integer price) {
        return price * quantity - (price * summDiscountByProductId / 100) * quantity;
    }

    //на входе массив объектов (id товара и количество)
    //возвращает объект, который содержит сумму заказа с учетом скидки и массив товаров(данные о продукте, количество, сумма без скидки)
    //скидка суммируется, но не более 50%
    //если вводишь id несуществующего товара - ошибка 400 с id товара
    @Override
    @Log
    public OrderInfo calculateOrder(OrderCalculationRequestList orderCalculationRequestList) {

        String deliveryAddress = orderCalculationRequestList.getDeliveryAddress();
        Map<String, String> request = Map.of("query", deliveryAddress);
        DadataResponse response = daDataClient.searchAddress("Token " + daDataApiKey, request);

        if (!response.getSuggestions().get(0).getData().getFias_level().equals("8")) {
            throw new NoSushAddressException(deliveryAddress);
        }

        int totalPrice = 0;
        List<Order> orders = new ArrayList<>();

        for (OrderCalculationRequest element : orderCalculationRequestList.getOrderCalculationRequestList()) {
            String productId = element.getId().toString();
            Product product = getProduct(productId);
            if (!product.isAvailable()) {
                continue;
            }
            Integer price = product.getPrice();

            //Получаем размер скидки по продукту
            Integer totalDiscountProduct = calculateDiscount(productId);

            Order order = new Order(element.getQuantity(), product);

            //Рассчитываем общую сумму заказа
            if (totalDiscountProduct == 0) {
                totalPrice += order.totalCost();
            } else {
                totalPrice += calculateCostWithDiscount(element.getQuantity(), totalDiscountProduct, price);
            }
            orders.add(order);
        }
        return new OrderInfo(totalPrice, orders);
    }
}
