package com.fatwire.cs.profiling.ss;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

public class BodyMarkerHandlerTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testPattern() {
        String input = "<page pagename=\"ComunidadMadrid/Comunes/Presentacion/pintaPromocionHome\" cachecontrol=\"\" cid=\"1142303858370\" language=\"es\" idDiv=\"caja_roja\" height=\"188\" width=\"477\" c=\"CM_PromocionHome_FA\" /page>";
        final Pattern pattern = Pattern
                .compile("(<page)(\\s(\\w*=\".*?\")?)*(/page>)");
        Matcher m = pattern.matcher(input + input+input+input);
        while (m.find()) {
            System.out.println(m.group());
        }
    }
}
