package com.github.oussamaxx.weasyprint.wrapper;

import com.github.oussamaxx.weasyprint.wrapper.exceptions.PDFExportException;
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
import java.io.InputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;


public class WeasyPrint {
    private static final Logger logger = LoggerFactory.getLogger(WeasyPrint.class);

    private static final String STDINOUT = "-";

    public static String weasyprintExecutableCommand = null;

    private Params params;

    private String htmlSource = null;

    private SourceType htmlSourceType = null;

    /**
     * Timeout to wait while generating a PDF, in seconds
     */
    private int timeout = 10;

    private List<Integer> successValues = new ArrayList<>(Collections.singletonList(0));


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

    public WeasyPrint addParams(Param param, Param... params) {
        this.params.add(param, params);
        return this;
    }

    public WeasyPrint addParams(Params params) {
        this.params.add(params);
        return this;
    }

    public WeasyPrint addParam(String key, String... valueArray) {
        this.params.add(new Param(key, valueArray));
        return this;
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
     * sets the html source from String
     *
     * @return the weasyprint instance
     */
    public WeasyPrint htmlFromString(String source){
        return html(source, SourceType.STRING);
    }




    /**
     * Generates a PDF file as byte array from the weasyprint output and save the result as a file
     *
     * @param path The path to the file where the PDF will be saved.
     * @return the saved file File Object
     * @throws IOException when not able to save the file
     * @throws InterruptedException when the PDF generation process got interrupted
     */
    public File writePDF(String path) throws IOException, InterruptedException {
        return writeFile(path, Format.PDF);
    }

    /**
     * Generates a PNG file as byte array from the weasyprint output and save the result as a file
     *
     * @param path The path to the file where the PNG will be saved.
     * @return the saved file File Object
     * @throws IOException when not able to save the file
     * @throws InterruptedException when the PNG generation process got interrupted
     */
    public File writePNG(String path) throws IOException, InterruptedException {
        return writeFile(path, Format.PNG);
    }


    private File writeFile(String path, Format format) throws IOException, InterruptedException {
        File file = new File(path);
        FileUtils.writeByteArrayToFile(file, getFileBytes(format));
        logger.info("File successfully saved in {}", file.getAbsolutePath());
        return file;
    }

    /**
     * Executes the weasyprint saving the PDF file directly to the specified file path.
     *
     * @param path The path to the file where the PDF file will be saved.
     * @return the PDF file saved
     * @throws IOException when not able to save the file
     * @throws InterruptedException when the PDF file generation process got interrupted
     */
    public File writePDFAsDirect(String path) throws IOException, InterruptedException {
        return writeFileAsDirect(path, Format.PDF);
    }

    /**
     * Executes the weasyprint saving the PNG file directly to the specified file path.
     *
     * @param path The path to the file where the PNG file will be saved.
     * @return the PNG file saved
     * @throws IOException when not able to save the file
     * @throws InterruptedException when the PNG file generation process got interrupted
     */
    public File writePNGAsDirect(String path) throws IOException, InterruptedException {
        return writeFileAsDirect(path, Format.PNG);
    }

    /**
     * Executes the weasyprint saving the results directly to the specified file path.
     *
     * @param path The path to the file where the file will be saved.
     * @return the file saved
     * @throws IOException when not able to save the file
     * @throws InterruptedException when the file generation process got interrupted
     */
    public File writeFileAsDirect(String path, Format format) throws IOException, InterruptedException {
        File file = new File(path);
        executeProcess(file.getAbsolutePath(), format);
        return file;
    }


    /**
     * Generates a PDF file as byte array from the weasyprint output
     *
     * @return the PDF file as a byte array
     * @throws IOException when not able to save the file
     * @throws InterruptedException when the PDF generation process got interrupted
     * @throws PDFExportException when the weasyprint process fails
     */
    public byte[] getPDF() throws IOException, InterruptedException, PDFExportException {
        return getFileBytes(Format.PDF);
    }

    /**
     * Generates a PNG file as byte array from the weasyprint output
     *
     * @return the PNG file as a byte array
     * @throws IOException when not able to save the file
     * @throws InterruptedException when the PNG generation process got interrupted
     * @throws PDFExportException when the weasyprint process fails
     */
    public byte[] getPNG() throws IOException, InterruptedException, PDFExportException {
        return getFileBytes(Format.PNG);
    }

    /**
     * return file(PDF/PNG) as byte array from the weasyprint output
     *
     * @return the process inputStream as bytes
     * @throws IOException when not able to save the file
     * @throws InterruptedException when the generation process got interrupted
     * @throws PDFExportException when the weasyprint process fails
     */
    public byte[] getFileBytes(Format format) throws IOException, InterruptedException, PDFExportException{
        return executeProcess(STDINOUT, format);
    }

    /**
     * return the process inputStream as bytes
     *
     * @return the process inputStream as bytes
     * @throws IOException when not able to save the file
     * @throws InterruptedException when the generation process got interrupted
     * @throws PDFExportException when the weasyprint process fails
     */
    private byte[] executeProcess(String outputFilename, Format format) throws IOException, InterruptedException, PDFExportException {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            String command = getCommand(outputFilename, format);
            logger.debug("Generating pdf with: {}", command);
            Process process = Runtime.getRuntime().exec(getCommandAsArray(outputFilename, format));

            Future<byte[]> inputStreamToByteArray = executor.submit(streamToByteArrayTask(process.getInputStream()));
            Future<byte[]> errorStreamToByteArray = executor.submit(streamToByteArrayTask(process.getErrorStream()));

            if(htmlSourceType == SourceType.STRING){
                OutputStream stdin = process.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
                writer.write(htmlSource);
                writer.flush();
                writer.close();
            }

            process.waitFor();

            if (!successValues.contains(process.exitValue())) {
                byte[] errorStream = getFuture(errorStreamToByteArray);
                logger.error("Error while generating pdf: {}", new String(errorStream));
                throw new PDFExportException(command, process.exitValue(), errorStream, getFuture(inputStreamToByteArray));
            } else {
                logger.debug("weasyprint output:\n{}", new String(getFuture(errorStreamToByteArray)));
            }

            logger.info("PDF successfully generated with: {}", command);
            return getFuture(inputStreamToByteArray);
        } finally {
            logger.debug("Shutting down executor for weasyprint.");
            executor.shutdownNow();
        }
    }

    private Callable<byte[]> streamToByteArrayTask(final InputStream input) {
        return new Callable<byte[]>() {
            public byte[] call() throws Exception {
                return IOUtils.toByteArray(input);
            }
        };
    }

    private byte[] getFuture(Future<byte[]> future) {
        try {
            return future.get(this.timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get command as array string
     *
     * @return the weasyprint command as string array
     */
    protected String[] getCommandAsArray(String outputFilename, Format format) {
        List<String> commandLine = new ArrayList<>();

        commandLine.add(weasyprintExecutableCommand);

        // weird behaviour when adding the space after "-f" weasyprint doesn't recognise the pdf type and thinks its
        // ' pdf' rather than 'pdf'
        commandLine.add("-f" + format.label);

        commandLine.addAll(params.getParamsAsStringList());

        // set encoding param to utf8 to fix blanc page when using STDIN
        // todo: add utf8 constant in the instance
        if(htmlSourceType == SourceType.STRING){
            commandLine.add("-e utf8");
        }

        commandLine.add(htmlSourceType == SourceType.STRING ?  STDINOUT : htmlSource);

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
