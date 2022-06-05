package io.github.oussamaxx.weasyprint.wrapper.params;

public enum Symbol {

    SEPARATOR(" "), PARAM("");

    private final String symbolStr;

    Symbol(String symbol) {
        this.symbolStr = symbol;
    }

    @Override
    public String toString() {
        return symbolStr;
    }

}
