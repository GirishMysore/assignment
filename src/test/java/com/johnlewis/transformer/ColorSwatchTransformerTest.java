package com.johnlewis.transformer;

import com.johnlewis.domain.ColorSwatch;
import com.johnlewis.domain.Product;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


public class ColorSwatchTransformerTest {

    private ColorSwatchTransformer colorSwatchTransformer = new ColorSwatchTransformer();

    @Test
    public void transform() {
        ColorSwatch redColorSwatch = ColorSwatch.builder()
                .color("red")
                .basicColor("Red")
                .skuId("redsku")
                .build();

        ColorSwatch unKnownSwatch = ColorSwatch.builder()
                .color("unknown")
                .basicColor("multi")
                .skuId("some")
                .build();

        Product product = Product.builder()
                .colorSwatches(Arrays.asList(redColorSwatch, unKnownSwatch))
                .build();


        assertThat(colorSwatchTransformer.transform(product))
                .flatExtracting(com.johnlewis.model.ColorSwatch::getColor, com.johnlewis.model.ColorSwatch::getRgbColor, com.johnlewis.model.ColorSwatch::getSkuid)
                .contains("red", "#FF0000", "redsku")
                .contains("unknown", "Unknown", "some");
    }

}