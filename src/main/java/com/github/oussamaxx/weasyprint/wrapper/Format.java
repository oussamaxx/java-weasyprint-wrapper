package com.github.oussamaxx.weasyprint.wrapper;

public enum Format {
    PDF("pdf"), PNG("png");

    Format(String label) {
        this.label = label;
    }

    String label;

}
