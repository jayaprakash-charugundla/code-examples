package com.spring.boot.excel.demo;

import com.spring.boot.excel.demo.dto.Product;
import com.spring.boot.excel.demo.dto.QuoteItem;

import java.util.ArrayList;
import java.util.List;

public class QuotationDataSupplier {
    public static List<QuoteItem> loadQuoteData() {
        List<QuoteItem> list = new ArrayList<>();
        QuoteItem item1 = new QuoteItem();
        item1.setQty(6);
        Product prd1 = new Product();
        prd1.setName("Craftatoz Wooden Chair");
        prd1.setPrice(399);
        item1.setProduct(prd1);
        list.add(item1);

        QuoteItem item2 = new QuoteItem();
        item2.setQty(1);
        Product prd2 = new Product();
        prd2.setName("Wish Chair - Beech / Natural");
        prd2.setPrice(299);
        item2.setProduct(prd2);
        list.add(item2);

        return list;
    }
}
