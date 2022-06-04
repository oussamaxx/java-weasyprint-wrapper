package com.github.oussamaxx.weasyprint.wrapper.unit;

import com.github.oussamaxx.weasyprint.wrapper.WeasyPrint;
import com.github.oussamaxx.weasyprint.wrapper.params.Param;
import com.github.oussamaxx.weasyprint.wrapper.params.Params;
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
        Params params = new Params();
        params.add("--stylesheet", "test.css");
        Assert.assertEquals(1, params.getParams().size());
        Assert.assertThat(params.toString(), containsString("--stylesheet test.css"));

        wp.addParams(new Param("--optimize-images"), new Param("--attachment", "test.txt"))
          .addParam("-a", "oui.pdf")
          .addParams(params);

        Assert.assertThat("command params should contain the --optimize-images , --attachment , -a --stylesheet test.css",
                wp.getCommand(), containsString("--optimize-images --attachment test.txt -a oui.pdf --stylesheet test.css"));

        wp.setParams(params);

        Assert.assertThat("command params should now only contain --stylesheet param",
                wp.getCommand(), containsString("--stylesheet test.css"));
    }
    @Test
    public void testParam() {
        Param p = new Param("-f", "pdf");
        Assert.assertEquals("-f", p.getKey());
    }
}
