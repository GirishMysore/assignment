package com.johnlewis.service;

import com.johnlewis.domain.Price;
import com.johnlewis.model.LabelType;
import com.johnlewis.model.Product;
import com.johnlewis.repository.ProductRepository;
import com.johnlewis.transformer.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private PriceTransformer priceTransformer;
    private ShowWasNowTransformer showWasNowTransformer;
    private ShowWasThenNowTransformer showWasThenNowTransformer;
    private ShowPercentageDiscountTransformer showPercentageDiscountTransformer;
    private ColorSwatchTransformer colorSwatchTransformer;

    public ProductService(ProductRepository productRepository,
                          PriceTransformer priceTransformer,
                          ShowWasNowTransformer showWasNowTransformer,
                          ShowWasThenNowTransformer showWasThenNowTransformer,
                          ShowPercentageDiscountTransformer showPercentageDiscountTransformer,
                          ColorSwatchTransformer colorSwatchTransformer) {
        this.productRepository = productRepository;
        this.priceTransformer = priceTransformer;
        this.showWasNowTransformer = showWasNowTransformer;
        this.showWasThenNowTransformer = showWasThenNowTransformer;
        this.showPercentageDiscountTransformer = showPercentageDiscountTransformer;
        this.colorSwatchTransformer = colorSwatchTransformer;
    }

    public List<Product> getProducts(Integer categoryId, LabelType labelType) {
        return productRepository.getProductsByCategoryId(categoryId)
                .stream()
                .filter(com.johnlewis.domain.Product::hasPriceReduction)
                .sorted()
                .map(product-> convertDomainToModel(product, labelType))
                .collect(Collectors.toList());
    }

    private Product convertDomainToModel(com.johnlewis.domain.Product product, LabelType labelType) {
        return Product.builder()
                .productId(product.getProductId())
                .title(product.getTitle())
                .colorSwatches(colorSwatchTransformer.transform(product))
                .nowPrice(priceTransformer.transform(product.getPrice().nowPrice(), product.getPrice().getCurrency()))
                .priceLabel(buildPriceLabel(product.getPrice(), labelType))
                .build();
    }

    private String buildPriceLabel(Price price, LabelType labelType) {
        switch (labelType) {
            case ShowWasThenNow: return showWasThenNowTransformer.transform(price);
            case ShowPercentageDiscount: return showPercentageDiscountTransformer.transform(price);
            default: return showWasNowTransformer.transform(price);
        }
    }

}
