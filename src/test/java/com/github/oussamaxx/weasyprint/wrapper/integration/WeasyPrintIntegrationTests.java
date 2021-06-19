package com.github.oussamaxx.weasyprint.wrapper.integration;
import com.github.oussamaxx.weasyprint.wrapper.WeasyPrint;
import org.junit.Assert;
import org.junit.Test;


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
}
