package com.johnlewis.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product implements Comparable<Product> {

    private String productId;
    private String title;
    private Price price;
    private List<ColorSwatch> colorSwatches = new ArrayList<>();

    public boolean hasPriceReduction() {
        BigDecimal was = price.wasAsBigDecimal();
        BigDecimal now = price.nowAsBigDecimal();
        return now.compareTo(was) < 0;
    }

    @Override
    public int compareTo(Product that) {
        return that.price.getReducedAmount().compareTo(this.price.getReducedAmount());
    }
}
