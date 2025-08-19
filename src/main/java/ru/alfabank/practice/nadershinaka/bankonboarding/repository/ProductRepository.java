package ru.alfabank.practice.nadershinaka.bankonboarding.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Product;

import java.util.List;


@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findAllByAvailableIsTrue();
    List<Product> findAllByAvailableIsFalse();
}
