package io.github.oussamaxx.weasyprint.wrapper.exceptions;


/**
 * Exception to describe and track wrapper configuration errors
 */
public class WeasyPrintConfigurationException extends RuntimeException {

    public WeasyPrintConfigurationException(String s) {
        super(s);
    }
    public WeasyPrintConfigurationException(String s, Throwable t) {
        super(s, t);
    }

}
