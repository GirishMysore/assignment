package com.johnlewis.repository;


import com.google.common.collect.Sets;
import com.johnlewis.domain.Product;
import com.johnlewis.domain.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Set;

@Slf4j
@Repository
public class ProductRepository {

    private static final String V1_CATEGORY_URL = "/v1/categories/{categoryId}/products?key=2ALHCAAs6ikGRBoy6eTHA58RaG097Fma";

    private final RestTemplate restTemplate;

    public ProductRepository(RestTemplate restTemplate) {
        this.restTemplate= restTemplate;
    }

    public Set<Product> getProductsByCategoryId(Integer categoryId) {
        Category category = restTemplate.getForObject(V1_CATEGORY_URL, Category.class, categoryId);
        return category != null ? Sets.newHashSet(category.getProducts()) : Collections.emptySet();
    }

}
