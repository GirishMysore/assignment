package com.johnlewis.transformer;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    private static final Map<String, String> COLOR_MAP = new HashMap<String, String>() {{
        put("Black", "#000000");
        put("White", "#FFFFFF");
        put("Red", "#FF0000");
        put("Lime", "#00FF00");
        put("Blue", "#0000FF");
        put("Yellow", "#FFFF00");
        put("Cyan", "#00FFFF");
        put("Aqua", "#00FFFF");
        put("Magenta", "#FF00FF");
        put("Fuchsia", "#FF00FF");
        put("Silver", "#C0C0C0");
        put("Grey", "#808080");
        put("Maroon", "#800000");
        put("Olive", "#808000");
        put("Green", "#008000");
        put("Purple", "#800080");
        put("Teal", "#008080");
        put("Navy", "#000080");
        put("Pink", "#FFC0CB");
    }};


    private Utils() {
    }

    @NonNull
    public static BigDecimal convertToBigDecimal(String in) {
        try {
            return new BigDecimal(in);
        } catch (NumberFormatException | NullPointerException e) {
            return BigDecimal.ZERO;
        }
    }

    @NonNull
    public static String basicColorToRgbColour(String basicColor) {
        return COLOR_MAP.getOrDefault(basicColor, "Unknown");
    }
}
