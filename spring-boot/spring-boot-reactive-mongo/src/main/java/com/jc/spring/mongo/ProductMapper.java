package com.jc.spring.mongo;

import com.jc.spring.mongo.dto.ProductDto;
import com.jc.spring.mongo.model.Product;
import org.springframework.beans.BeanUtils;

public class ProductMapper {

    public static ProductDto toProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        return productDto;
    }

    public static Product toProduct(ProductDto productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        return product;
    }
}
