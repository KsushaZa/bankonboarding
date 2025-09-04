package ru.alfabank.practice.nadershinaka.bankonboarding.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.alfabank.practice.nadershinaka.bankonboarding.entity.Discount;

import java.util.List;

@Repository
public interface DiscountRepository extends MongoRepository<Discount, String> {

    // Используем $in для поиска productId в productList
    @Query("{ 'productList': { $in: [?0] } }")
    List<Discount> findByProductId(String productId);

}
