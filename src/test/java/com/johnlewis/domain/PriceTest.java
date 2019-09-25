package com.johnlewis.domain;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PriceTest {
    
    @Test
    public void nowPriceMapTypeGetToElement() {
        Map map = new HashMap(){{put("to", "10");}};
        assertThat(Price.builder().now(map).build().nowPrice())
                .isEqualTo("10");
    }

    @Test
    public void nowPriceStringType() {
        assertThat(Price.builder().now("20").build().nowPrice())
                .isEqualTo("20");
    }
    @Test
    public void getReducedAmount() {
        assertThat(Price.builder().was("30").now("25").build().getReducedAmount())
                .isEqualTo(new BigDecimal("5"));
    }

}