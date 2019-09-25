package com.johnlewis.transformer;

import com.johnlewis.domain.Price;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ShowPercentageDiscountTransformer {

    private static final String PERCENTAGE_DISCOUNT_FORMAT = "%s%% off - now %s";

    private PriceTransformer priceTransformer;

    public ShowPercentageDiscountTransformer(PriceTransformer priceTransformer) {
        this.priceTransformer = priceTransformer;
    }

    public String transform(Price price) {
        BigDecimal was = Utils.convertToBigDecimal(price.getWas());

        if(was.compareTo(BigDecimal.ZERO) <= 0 || price.getReducedAmount().compareTo(BigDecimal.ZERO) < 0) {
            return "0";
        }
        int discount = price.getReducedAmount().divide(was, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.UP).intValue();
        return String.format(PERCENTAGE_DISCOUNT_FORMAT, discount, priceTransformer.transform(price.nowPrice(), price.getCurrency()));
    }


}
