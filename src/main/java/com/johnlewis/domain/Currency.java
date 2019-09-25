package com.johnlewis.domain;


public enum Currency {

    GBP("£"), USD("$"), EUR("€");

    private String symbol;


    public String value() {
        return symbol;
    }
    Currency(String symbol){
        this.symbol = symbol;
    }
}
