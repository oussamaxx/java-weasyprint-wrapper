package com.github.oussamaxx.weasyprint.wrapper.unit;

import com.github.oussamaxx.weasyprint.wrapper.WeasyPrint;
import com.github.oussamaxx.weasyprint.wrapper.params.Param;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.StringContains.containsString;

public class WeasyPrintUnitTests {
    private WeasyPrint wp;

    @Before
    public void setUp() {
        wp = new WeasyPrint();
    }

    @Test
    public void testParams() {

        wp.addParams(new Param("--optimize-images"), new Param("--attachment", "test.txt"))
          .addParam("-a", "oui.pdf");

        Assert.assertThat("command params should contain the --optimize-images , --attachment and -a",
                wp.getCommand(), containsString("--optimize-images --attachment test.txt -a oui.pdf"));
    }
}
