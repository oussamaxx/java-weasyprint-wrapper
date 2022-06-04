package com.github.oussamaxx.weasyprint.wrapper.integration;

import com.github.oussamaxx.weasyprint.wrapper.SourceType;
import com.github.oussamaxx.weasyprint.wrapper.WeasyPrint;
import com.github.oussamaxx.weasyprint.wrapper.exceptions.PDFExportException;
import com.github.oussamaxx.weasyprint.wrapper.exceptions.WeasyPrintConfigurationException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.core.StringContains.containsString;

public class WeasyPrintIntegrationTests {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private final String leagacyWeasyExecutable = ".\\legacy_venv\\Scripts\\weasyprint.exe";
    @Test
    public void testWeasyPrintExecutableFinder()
    {
        WeasyPrint.foundedWeasyprintExecutableCommand = null;
        Assert.assertNull(WeasyPrint.foundedWeasyprintExecutableCommand);
        WeasyPrint wp1 = new WeasyPrint(false);
        String commandAfterInitFirstObj = WeasyPrint.foundedWeasyprintExecutableCommand;
        Assert.assertNotNull(commandAfterInitFirstObj);
        WeasyPrint wp2 = new WeasyPrint();
        String commandAfterInitSecondObj = WeasyPrint.foundedWeasyprintExecutableCommand;
        Assert.assertEquals(commandAfterInitFirstObj, commandAfterInitSecondObj);
    }

    @Test
    public void testHtmlInputTypes(){
        WeasyPrint wp = new WeasyPrint();
        String command = wp.html("http://google.com", SourceType.URL).getCommand("google.pdf");
        System.out.println("command 1: " + command);
        Assert.assertThat("command params should contain the http://google.com and google.pdf",
                command, containsString("http://google.com google.pdf"));

        wp.htmlFromFile("file.html");
        System.out.println("command 2: " + wp.getCommand());
        Assert.assertThat("command params should contain file.html",
                wp.getCommand(), containsString("file.html"));

    }
    @Test
    public void generateGooglePDFFileBytes() throws IOException, InterruptedException {
        WeasyPrint wp = new WeasyPrint();
        byte[] result = wp.html("http://google.com", SourceType.URL).getPDF();
        String resultAsString = getPdfText(result);

        Assert.assertThat("the generated file bytes have Google",
                resultAsString, containsString("Google"));

    }
    @Test
    public void generatePNGWithNoLegacy() throws IOException, InterruptedException {
        WeasyPrint wp = new WeasyPrint(leagacyWeasyExecutable);
        exception.expect(WeasyPrintConfigurationException.class);
        byte[] result = wp.html("http://google.com", SourceType.URL).getPNG();
    }

    @Test
    public void testWritePDF() throws IOException, InterruptedException {
        WeasyPrint wp = new WeasyPrint();
        File saved_file  = wp.htmlFromURL("http://google.com").writePDF("test_google.pdf");
        Assert.assertTrue(saved_file.exists());
    }

    @Test
    public void testWritePNG() throws IOException, InterruptedException {
        WeasyPrint wp = new WeasyPrint();
        exception.expect(WeasyPrintConfigurationException.class);
        File saved_file  = wp.htmlFromURL("http://google.com").writePNG("test_google.png");
        //Assert.assertTrue(saved_file.exists());
    }

    @Test
    public void testWritePDFAsDirect() throws IOException, InterruptedException {
        WeasyPrint wp = new WeasyPrint();
        File saved_file  = wp.htmlFromURL("http://google.com").writePDFAsDirect("test_google_direct.pdf");
        Assert.assertTrue(saved_file.exists());
    }

    @Test
    public void testStringAsInput() throws IOException, InterruptedException {
        WeasyPrint wp = new WeasyPrint();
        byte[] result  = wp.htmlFromString("<html><b>uwu</b></html>")
                .getPDF();
        String resultAsString = getPdfText(result);
        Assert.assertThat("the generated file bytes have uwu",
                resultAsString, containsString("uwu"));
    }

    @Test
    public void testStringAsInputAndSave() throws IOException, InterruptedException {
        WeasyPrint wp = new WeasyPrint();
        File saved_file = wp.htmlFromString("<html><b>uwu</b></html>").writePDF("uwu_pdf.pdf");
        // check if the file exists
        Assert.assertTrue("the file exists", saved_file.exists());
        // check if the file contains uwu
        String resultAsString = getPdfText(saved_file);
        Assert.assertThat("the generated file bytes have uwu",
                resultAsString, containsString("uwu"));
    }
    @Test
    public void testSuccessValues() throws IOException, InterruptedException {
        WeasyPrint wp = new WeasyPrint();
        // overriding the default 0 in the success values
        wp.setSuccessValues(Arrays.asList(4,5));
        exception.expect(PDFExportException.class);
        byte[] result = wp.htmlFromURL("http://google.com").getPDF();
    }

    private String getPdfText(byte[] pdfBytes) throws IOException {
        PDDocument pdDocument = PDDocument.load(new ByteArrayInputStream(pdfBytes));
        String text = new PDFTextStripper().getText(pdDocument);

        pdDocument.close();
        return text;
    }
    private String getPdfText(File pdfFile) throws IOException {
        PDDocument pdDocument = PDDocument.load(pdfFile);
        String text = new PDFTextStripper().getText(pdDocument);

        pdDocument.close();
        return text;
    }
}
