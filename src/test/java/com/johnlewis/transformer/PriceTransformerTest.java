package com.johnlewis.transformer;

import com.johnlewis.domain.Currency;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class PriceTransformerTest {

    private PriceTransformer priceTransformer = new PriceTransformer();

    @Test
    public void currencyIsNotSetDefaultsToGBP() {
        assertThat(priceTransformer.transform("1", null))
                .isEqualTo("£1.00");
    }

    @Test
    public void lessThanTenWithoutDecimalDigit() {
        assertThat(priceTransformer.transform("1", Currency.GBP))
                .isEqualTo("£1.00");
    }

    @Test
    public void lessThanTenWithSingleDecimalDigit() {
        assertThat(priceTransformer.transform("1.1", Currency.GBP))
                .isEqualTo("£1.10");
    }
    @Test
    public void lessThanTenWithTwoDecimalDigit() {
        assertThat(priceTransformer.transform("1.12", Currency.GBP))
                .isEqualTo("£1.12");
    }

    @Test
    public void lessThanTenWithMoreThanTwoDecimalDigit() {
        assertThat(priceTransformer.transform("1.123", Currency.GBP))
                .isEqualTo("£1.13");
    }

    @Test
    public void equalToTen() {
        assertThat(priceTransformer.transform("10", Currency.GBP))
                .isEqualTo("£10");
    }


    @Test
    public void greaterThanTen() {
        assertThat(priceTransformer.transform("10.1", Currency.GBP))
                .isEqualTo("£10");
    }
    

    @Test
    public void notANumber() {
        assertThat(priceTransformer.transform("safdaf", Currency.GBP))
                .isEqualTo("£0.00");
    }

    @Test
    public void empty() {
        assertThat(priceTransformer.transform(" ", Currency.GBP))
                .isEqualTo("£0.00");
    }


}