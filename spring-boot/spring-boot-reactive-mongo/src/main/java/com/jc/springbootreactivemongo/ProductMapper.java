package com.jc.springbootreactivemongo;

import com.jc.springbootreactivemongo.dto.ProductDto;
import com.jc.springbootreactivemongo.model.Product;
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
