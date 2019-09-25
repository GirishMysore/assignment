package com.johnlewis.transformer;

import com.johnlewis.domain.Product;
import com.johnlewis.model.ColorSwatch;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ColorSwatchTransformer {

    public List<ColorSwatch> transform(Product product) {
        return product.getColorSwatches()
                .stream()
                .map(this::to)
                .collect(Collectors.toList());
    }

    private ColorSwatch to(com.johnlewis.domain.ColorSwatch in) {
        return ColorSwatch.builder()
                .color(in.getColor())
                .rgbColor(Utils.basicColorToRgbColour(in.getBasicColor()))
                .skuid(in.getSkuId())
                .build();
    }

}
