package com.johnlewis.transformer;

import com.johnlewis.domain.Price;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ShowWasThenNowTransformer {

    private static final String WAS_THEN_NOW_FORMAT = "Was %s,%snow %s";
    private static final String THEN_FORMAT = " then %s, ";

    private PriceTransformer priceTransformer;

    public ShowWasThenNowTransformer(PriceTransformer priceTransformer) {
        this.priceTransformer = priceTransformer;
    }


    public String transform(Price price) {
        String was = priceTransformer.transform(price.getWas(), price.getCurrency());
        String now = priceTransformer.transform(price.nowPrice(), price.getCurrency());

        return String.format(WAS_THEN_NOW_FORMAT, was, getThen(price), now);
    }

    private String getThen(Price price) {
        if(StringUtils.isEmpty(price.getThen1()) && StringUtils.isEmpty(price.getThen2())) {
            return " ";
        } else if(!StringUtils.isEmpty(price.getThen2())) {
            return String.format(THEN_FORMAT, priceTransformer.transform(price.getThen2(), price.getCurrency()));
        }
        return String.format(THEN_FORMAT, priceTransformer.transform(price.getThen1(), price.getCurrency()));
    }
}
