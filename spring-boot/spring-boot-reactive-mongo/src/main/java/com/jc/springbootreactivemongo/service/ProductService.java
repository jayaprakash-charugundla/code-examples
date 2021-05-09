package com.jc.springbootreactivemongo.service;

import com.jc.springbootreactivemongo.ProductMapper;
import com.jc.springbootreactivemongo.dto.ProductDto;
import com.jc.springbootreactivemongo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Flux<ProductDto> getProducts() {
        return productRepository.findAll().map(ProductMapper::toProductDto);
    }

    public Mono<ProductDto> getProduct(String id) {
        return productRepository.findById(id).map(ProductMapper::toProductDto);
    }

    public Flux<ProductDto> getProductInRange(double min, double max) {
        return productRepository.findByPriceBetween(Range.closed(min, max));
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono) {
        return productDtoMono.map(ProductMapper::toProduct)
                .flatMap(productRepository::insert)
                .map(ProductMapper::toProductDto);
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id) {
        return productRepository.findById(id)
                .flatMap(product -> productDtoMono.map(ProductMapper::toProduct))
                .doOnNext(e -> e.setId(id))
                .flatMap(productRepository::save)
                .map(ProductMapper::toProductDto);
    }

    public Mono<Void> deleteProduct(String id) {
        return productRepository.deleteById(id);
    }
}
