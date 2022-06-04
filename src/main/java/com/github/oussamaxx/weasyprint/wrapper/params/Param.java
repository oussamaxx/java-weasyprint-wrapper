package com.github.oussamaxx.weasyprint.wrapper.params;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Param {

    private String key;

    private List<String> values = new ArrayList<>();

    public Param(String key, String... valueArray) {
        this.key = key;
        values.addAll(Arrays.asList(valueArray));
    }

    public Param(String key) {
        this(key, new String[0]);
    }

    public String getKey() {
        return key;
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append(Symbol.SEPARATOR)
                .append(Symbol.PARAM).append(key);
        for (String value : values)
            sb.append(Symbol.SEPARATOR).append(value);
        return sb.toString();
    }

}
