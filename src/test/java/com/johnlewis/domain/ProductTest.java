package com.johnlewis.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ProductTest {


    @Test
    public void priceReducedWhenNowPriceIsLessThanWasPrice() {
        Product product = Product.builder().price(Price.builder().now("10").was("20").build()).build();
        assertThat(product.hasPriceReduction())
                .isTrue();
    }

    @Test
    public void priceNotReducedWhenNowPriceIsGreaterThanWasPrice() {
        Product product = Product.builder().price(Price.builder().now("30").was("20").build()).build();
        assertThat(product.hasPriceReduction())
                .isFalse();
    }

    @Test
    public void priceNotReducedWhenNowPriceIsEqualToWasPrice() {
        Product product = Product.builder().price(Price.builder().now("30").was("30").build()).build();
        assertThat(product.hasPriceReduction())
                .isFalse();
    }

    @Test
    public void priceNotReducedWhenWasPriceIsEmpty() {
        Product product = Product.builder().price(Price.builder().now("30").build()).build();
        assertThat(product.hasPriceReduction())
                .isFalse();
    }

    @Test
    public void priceReducedWhenWasPriceIsNotEmptyAndNowPriceIsEmpty() {
        Product product = Product.builder().price(Price.builder().was("30").build()).build();
        assertThat(product.hasPriceReduction())
                .isTrue();
    }

    @Test
    public void priceNotReducedWhenWasAndNowPriceIsEmpty() {
        Product product = Product.builder().price(Price.builder().build()).build();
        assertThat(product.hasPriceReduction())
                .isFalse();
    }

}