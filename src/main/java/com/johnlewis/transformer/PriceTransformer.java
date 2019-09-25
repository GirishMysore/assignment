package com.johnlewis.transformer;

import com.johnlewis.domain.Currency;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PriceTransformer {

    @NonNull
    public String transform(String value, Currency currency) {
        currency = currency == null ? Currency.GBP : currency;
        BigDecimal to = Utils.convertToBigDecimal(value);
        return currency.value() + formatPrice(to);
    }

    private static String formatPrice(BigDecimal now) {
        return now.intValue() < 10 ? now.setScale(2, RoundingMode.UP).toString() : now.toBigInteger().toString();
    }


}
