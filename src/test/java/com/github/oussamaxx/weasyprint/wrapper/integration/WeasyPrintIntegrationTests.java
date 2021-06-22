package com.github.oussamaxx.weasyprint.wrapper.integration;
import com.github.oussamaxx.weasyprint.wrapper.Format;
import com.github.oussamaxx.weasyprint.wrapper.SourceType;
import com.github.oussamaxx.weasyprint.wrapper.WeasyPrint;
import org.junit.Assert;
import org.junit.Test;

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
        System.out.println("command:" + command);
        Assert.assertThat("command params should contain the http://google.com and google.pdf",
                command, containsString("http://google.com google.pdf"));
    }
}
