package com.github.oussamaxx.weasyprint.wrapper.integration;

import com.github.oussamaxx.weasyprint.wrapper.SourceType;
import com.github.oussamaxx.weasyprint.wrapper.WeasyPrint;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.StringContains.containsString;

public class WeasyPrintLegacyIntegrationTests {
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    private final String leagacyWeasyExecutable = ".\\legacy_venv\\Scripts\\weasyprint.exe";

    @Test
    public void testWritePNG() throws IOException, InterruptedException {
        WeasyPrint wp = new WeasyPrint(leagacyWeasyExecutable, true);
        wp.setTimeout(100);
        File saved_file  = wp.htmlFromURL("http://google.com").writePNG("test_google.png");
        Assert.assertTrue(saved_file.exists());
    }
    @Test
    public void generateGooglePDFFileBytes() throws IOException, InterruptedException {
        WeasyPrint wp = new WeasyPrint(leagacyWeasyExecutable, true);
        wp.setIsLegacy(true);
        byte[] result = wp.html("http://google.com", SourceType.URL).getPDF();
        String resultAsString = getPdfText(result);

        Assert.assertThat("the generated file bytes have Google",
                resultAsString, containsString("Google"));

    }
    @Test
    public void generatePNGBytes() throws IOException, InterruptedException {
        WeasyPrint wp = new WeasyPrint(leagacyWeasyExecutable, true);
        byte[] result = wp.html("http://google.com", SourceType.URL).getPNG();
        Assert.assertNotNull(result);
    }
    @Test
    public void testWritePNGAsDirect() throws IOException, InterruptedException {
        WeasyPrint wp = new WeasyPrint(leagacyWeasyExecutable, true);
        File saved_file  = wp.htmlFromURL("http://google.com").writePNGAsDirect("test_google_direct.png");
        Assert.assertTrue(saved_file.exists());
    }


    private String getPdfText(byte[] pdfBytes) throws IOException {
        PDDocument pdDocument = PDDocument.load(new ByteArrayInputStream(pdfBytes));
        String text = new PDFTextStripper().getText(pdDocument);

        pdDocument.close();
        return text;
    }
}
