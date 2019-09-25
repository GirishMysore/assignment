package com.johnlewis.transformer;

import com.johnlewis.domain.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShowPercentageDiscountTransformerTest {

    @Mock
    private PriceTransformer priceTransformer;

    @InjectMocks
    private ShowPercentageDiscountTransformer showPercentageDiscountTransformer;


    @Test
    public void wasPriceZero() {
        Price price = Price.builder().now("10").build();
        assertThat(showPercentageDiscountTransformer.transform(price))
                .isEqualTo("0");
    }

    @Test
    public void wasPriceLessThanNowPrice() {
        Price price = Price.builder().was("5").now("10").build();
        assertThat(showPercentageDiscountTransformer.transform(price))
                .isEqualTo("0");
    }

    @Test
    public void wasPriceEqualToNowPrice() {
        Price price = Price.builder().was("10").now("10").build();
        when(priceTransformer.transform(price.nowPrice(), price.getCurrency())).thenReturn("£10");
        assertThat(showPercentageDiscountTransformer.transform(price))
                .isEqualTo("0% off - now £10");
    }

    @Test
    public void wasPriceGreaterThanNowPrice() {
        Price price = Price.builder().was("19").now("10").build();
        when(priceTransformer.transform(price.nowPrice(), price.getCurrency())).thenReturn("£10");
        assertThat(showPercentageDiscountTransformer.transform(price))
                .isEqualTo("47% off - now £10");
    }


    @Test
    public void wasPriceGreaterThanNowPriceAnother() {
        Price price = Price.builder().was("20").now("10").build();
        when(priceTransformer.transform(price.nowPrice(), price.getCurrency())).thenReturn("£10");
        assertThat(showPercentageDiscountTransformer.transform(price))
                .isEqualTo("50% off - now £10");
    }
}