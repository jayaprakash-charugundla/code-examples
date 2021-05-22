package com.jc.spring.mongo.repository;

import com.jc.spring.mongo.dto.ProductDto;
import com.jc.spring.mongo.model.Product;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    Flux<ProductDto> findByPriceBetween(Range<Double> priceRange);
}
