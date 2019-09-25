package com.johnlewis.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColorSwatch {
    private String color;
    private String rgbColor;
    private String skuid;

}
