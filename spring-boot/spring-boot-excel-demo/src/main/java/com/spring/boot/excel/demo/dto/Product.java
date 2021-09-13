package com.spring.boot.excel.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class Product {
    private String name;
    private List<String> features;
    private double price;
    private String imageFile;
}
