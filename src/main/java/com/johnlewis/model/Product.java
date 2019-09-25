package com.johnlewis.model;

import lombok.*;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    String productId;
    String title;
    String nowPrice;
    String priceLabel;
    List<ColorSwatch> colorSwatches;

}
