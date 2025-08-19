package ru.alfabank.practice.nadershinaka.bankonboarding.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductInitializer {

//    @Bean
//    CommandLineRunner initialize(ProductService productService, ProductRepository productRepository) {
//        return args -> {
//            if (productRepository.count() == 0) { // чтобы не дублировать
//                List<Product> products = productService.getAllProduct();
//                productRepository.saveAll(products);
//                System.out.println("Products added to MongoDB.");
//                System.out.println("!!!");
//            } else {
//                System.out.println("Продукты уже есть в базе — загрузка не требуется.");
//                System.out.println("???");
//            }
//        };
//    }
//}

//    @Bean
//    public CommandLineRunner initializeProducts(ProductService productService, ProductRepository productRepository) {
//        return args -> {
// // Получаем стандартный список
//            List<Product> defaultProducts = productService.getAllProduct();
//
//// Узнаём все существующие продукты
//            Set<String> existingIds = productRepository
//                    .findAll()
//                    .stream()
//                    .map(Product::getId)
//                    .collect(Collectors.toSet());
//
////Фильтруем — оставляем только тех, кого ещё нет
//            List<Product> toInsert = defaultProducts.stream()
//                    .filter(p -> !existingIds.contains(p.getId()))
//                    .collect(Collectors.toList());
//
////Сохраняем недостающих
//            if (!toInsert.isEmpty()) {
//                productRepository.saveAll(toInsert);
//                System.out.printf("done", toInsert.size());
//            } else {
//                System.out.println("exist");
//            }
//        };
//    }
}