package com.github.oussamaxx.weasyprint.wrapper.params;

public enum Symbol {

    separator(" "), param("");

    private final String symbol;

    Symbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

}
