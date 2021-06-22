package com.github.oussamaxx.weasyprint.wrapper;

import com.github.oussamaxx.weasyprint.wrapper.exceptions.WeasyPrintConfigurationException;
import com.github.oussamaxx.weasyprint.wrapper.params.Param;
import com.github.oussamaxx.weasyprint.wrapper.params.Params;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class WeasyPrint {
    private static final Logger logger = LoggerFactory.getLogger(WeasyPrint.class);

    private static final String STDINOUT = "-";


    public static String weasyprintExecutableCommand = null;

    private Params params;

    private String htmlSource = null;

    private SourceType htmlSourceType = null;


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
        params = new Params();
    }

    public void addParams(Param param, Param... params) {
        this.params.add(param, params);
    }

    public void addParams(Params params) {
        this.params.add(params);
    }

    public void addParam(String key, String... valueArray) {
        this.params.add(new Param(key, valueArray));
    }

    public void setParams(Params params) {
        this.params = params;
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

    /**
     * sets the html source
     *
     * @return the weasyprint instance
     */
    public WeasyPrint html(String source, SourceType type){
        htmlSource = source;
        htmlSourceType = type;
        return this;
    }

    /**
     * sets the html source from a URL
     *
     * @return the weasyprint instance
     */
    public WeasyPrint htmlFromURL(String source){
        return html(source, SourceType.URL);
    }

    /**
     * sets the html source from a file path
     *
     * @return the weasyprint instance
     */
    public WeasyPrint htmlFromFile(String source){
        return html(source, SourceType.FILE);
    }


    /**
     * Get command as array string
     *
     * @return the weasyprint command as string array
     */
    protected String[] getCommandAsArray(String outputFilename, Format format) {
        List<String> commandLine = new ArrayList<>();

        commandLine.add(weasyprintExecutableCommand);

        commandLine.add("-f " + format.label);

        commandLine.addAll(params.getParamsAsStringList());

        commandLine.add((htmlSource !=null) ? htmlSource : STDINOUT);

        commandLine.add((outputFilename != null) ? outputFilename : STDINOUT);
        logger.debug("Command generated: {}", commandLine.toString());
        return commandLine.toArray(new String[0]);
    }

    public String getCommand() {
        return getCommand(STDINOUT);
    }

    public String getCommand(String outputFilename) {
        return getCommand(outputFilename, Format.PDF);
    }

    /**
     * Gets the final weasyprint command as string
     *
     * @return the generated command from params
     */
    public String getCommand(String outputFilename, Format format) {
        return StringUtils.join(getCommandAsArray(outputFilename, format), " ");
    }


}
