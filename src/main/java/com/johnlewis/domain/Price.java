package com.johnlewis.domain;


import com.johnlewis.transformer.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Price {
    private String was;
    private String then1;
    private String then2;
    private Object now;
    private Currency currency;

    public String nowPrice() {
        if (now instanceof Map) {
            Map complexNow = (Map) now;
            return (String) complexNow.get("to");
        }
        return (String)now;
    }

    public BigDecimal wasAsBigDecimal() {
        return Utils.convertToBigDecimal(was);
    }

    public BigDecimal nowAsBigDecimal() {
        return Utils.convertToBigDecimal(nowPrice());
    }

    public BigDecimal getReducedAmount() {
        return wasAsBigDecimal().subtract(nowAsBigDecimal());
    }

}
