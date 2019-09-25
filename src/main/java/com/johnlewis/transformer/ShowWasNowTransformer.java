package com.johnlewis.transformer;


import com.johnlewis.domain.Price;
import org.springframework.stereotype.Component;

@Component
public class ShowWasNowTransformer {

    private static final String WAS_NOW_FORMAT = "Was %s, now %s";

    private PriceTransformer priceTransformer;


    public ShowWasNowTransformer(PriceTransformer priceTransformer) {
        this.priceTransformer = priceTransformer;
    }

    public String transform(Price price) {
        String was = priceTransformer.transform(price.getWas(), price.getCurrency());
        String now = priceTransformer.transform(price.nowPrice(), price.getCurrency());

        return String.format(WAS_NOW_FORMAT, was, now);
    }


}
