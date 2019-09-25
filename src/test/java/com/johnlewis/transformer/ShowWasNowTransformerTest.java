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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ShowWasNowTransformerTest {

    @Mock
    private PriceTransformer priceTransformer;

    @InjectMocks
    private ShowWasNowTransformer showWasNowTransformer;


    @Test
    public void showWasNow() {
        Price price = Price.builder().was("10").now("20").currency(Currency.GBP).build();
        when(priceTransformer.transform(anyString(), any(Currency.class))).thenReturn("£10").thenReturn("£20");

        assertThat(showWasNowTransformer.transform(price))
                .isEqualTo("Was £10, now £20");

        verify(priceTransformer).transform("10", Currency.GBP);
        verify(priceTransformer).transform("20", Currency.GBP);
    }

}