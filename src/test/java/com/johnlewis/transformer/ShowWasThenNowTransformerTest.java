package com.johnlewis.transformer;

import com.johnlewis.domain.Currency;
import com.johnlewis.domain.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShowWasThenNowTransformerTest {

    @Mock
    private PriceTransformer priceTransformer;

    @InjectMocks
    private ShowWasThenNowTransformer showWasThenNowTransformer;

    @Test
    public void showWasThenNowWhenThen1AndThen2AreNotEmpty() {
        Price price = Price.builder().was("10").now("20").then1("30").then2("40").currency(Currency.GBP).build();

        when(priceTransformer.transform(anyString(), any(Currency.class))).thenReturn("£10").thenReturn("£20").thenReturn("£40");

        assertThat(showWasThenNowTransformer.transform(price))
                .isEqualTo("Was £10, then £40, now £20");

        verify(priceTransformer).transform("10", Currency.GBP);
        verify(priceTransformer).transform("20", Currency.GBP);
        verify(priceTransformer).transform("40", Currency.GBP);
        verify(priceTransformer,times(0)).transform("30", Currency.GBP);
    }


    @Test
    public void showWasThenNowWhenThen2IsNotEmpty() {
        Price price = Price.builder().was("10").now("20").then2("40").currency(Currency.GBP).build();
        when(priceTransformer.transform(anyString(), any(Currency.class))).thenReturn("£10").thenReturn("£20").thenReturn("£40");

        assertThat(showWasThenNowTransformer.transform(price))
                .isEqualTo("Was £10, then £40, now £20");

        verify(priceTransformer).transform("10", Currency.GBP);
        verify(priceTransformer).transform("20", Currency.GBP);
        verify(priceTransformer).transform("40", Currency.GBP);
    }


    @Test
    public void showWasThenNowWhenThen1IsNotThen2IsEmpty() {
        Price price = Price.builder().was("10").now("20").then1("30").currency(Currency.GBP).build();
        when(priceTransformer.transform(anyString(), any(Currency.class))).thenReturn("£10").thenReturn("£20").thenReturn("£30");

        assertThat(showWasThenNowTransformer.transform(price))
                .isEqualTo("Was £10, then £30, now £20");

        verify(priceTransformer).transform("10", Currency.GBP);
        verify(priceTransformer).transform("20", Currency.GBP);
        verify(priceTransformer).transform("30", Currency.GBP);
    }

    @Test
    public void showWasThenNowWhenThen1AndThen2AreEmpty() {
        Price price = Price.builder().was("10").now("20").currency(Currency.GBP).build();
        when(priceTransformer.transform(anyString(), any(Currency.class))).thenReturn("£10").thenReturn("£20");

        assertThat(showWasThenNowTransformer.transform(price))
                .isEqualTo("Was £10, now £20");

        verify(priceTransformer,times(1)).transform("10", Currency.GBP);
        verify(priceTransformer,times(1)).transform("20", Currency.GBP);
    }

}