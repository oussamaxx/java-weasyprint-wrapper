package com.github.oussamaxx.weasyprint.wrapper;

import com.github.oussamaxx.weasyprint.wrapper.exceptions.WeasyPrintConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class WeasyPrint {
    private static final Logger logger = LoggerFactory.getLogger(WeasyPrint.class);
    public static String weasyprintExecutableCommand = null;


    /**
     * Default constructor with no given executable path
     */
    public WeasyPrint() {
        this(weasyprintExecutableCommand==null ? findExecutable() : weasyprintExecutableCommand);
    }

    /**
     * constructor with executable path given in the args
     * @param executable command/path to call the weasyprint executable
     */
    public WeasyPrint(String executable) {
        weasyprintExecutableCommand = executable;
    }

    /**
     * Attempts to find the `weasyprint` executable in the system path.
     *
     * @return the weasyprint executable command according to the OS
     */
    public static String findExecutable() {
        try {
            String osname = System.getProperty("os.name").toLowerCase();

            String cmd = osname.contains("windows") ? "where.exe weasyprint" : "which weasyprint";

            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();

            String text = IOUtils.toString(p.getInputStream(), Charset.defaultCharset()).trim();

            if (text.isEmpty())
                throw new WeasyPrintConfigurationException("weasyprint command was not found in your classpath. " +
                        "Verify its installation or initialize the WeasyPrint object with correct path/to/weasyprint");

            logger.debug("Wkhtmltopdf command found in classpath: {}", text);
            return text;
        } catch (InterruptedException | IOException e) {
            logger.error("Fatal:", e);
            throw new WeasyPrintConfigurationException("Failed while getting weasyprint executable.", e);
        }
    }
}
