package com.johnlewis.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ColorSwatch {
    private String color;
    private String basicColor;
    private String skuId;
}
