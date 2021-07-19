package com.github.oussamaxx.weasyprint.wrapper.integration;

import com.github.oussamaxx.weasyprint.wrapper.SourceType;
import com.github.oussamaxx.weasyprint.wrapper.WeasyPrint;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.StringContains.containsString;


public class WeasyPrintIntegrationTests {
    @Test
    public void testWeasyPrintExecutableFinder()
    {
        Assert.assertNull(WeasyPrint.weasyprintExecutableCommand);
        WeasyPrint wp1 = new WeasyPrint();
        String commandAfterInitFirstObj = WeasyPrint.weasyprintExecutableCommand;
        Assert.assertNotNull(commandAfterInitFirstObj);
        WeasyPrint wp2 = new WeasyPrint();
        String commandAfterInitSecondObj = WeasyPrint.weasyprintExecutableCommand;
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
        String resultAsString = getPdfTextFromBytes(result);

        Assert.assertThat("the generated file bytes have Google",
                resultAsString, containsString("Google"));

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
        File saved_file  = wp.htmlFromURL("http://google.com").writePNG("test_google.png");
        Assert.assertTrue(saved_file.exists());

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
        String resultAsString = getPdfTextFromBytes(result);
        Assert.assertThat("the generated file bytes have uwu",
                resultAsString, containsString("uwu"));
    }

    @Test
    public void testStringAsInputAndSave() throws IOException, InterruptedException {
        WeasyPrint wp = new WeasyPrint();
        wp.htmlFromString("<html><b>uwu</b></html>").writePDF("uwu_pdf.pdf");
    }

    private String getPdfTextFromBytes(byte[] pdfBytes) throws IOException {
        PDDocument pdDocument = PDDocument.load(new ByteArrayInputStream(pdfBytes));
        String text = new PDFTextStripper().getText(pdDocument);

        pdDocument.close();
        return text;
    }
}
