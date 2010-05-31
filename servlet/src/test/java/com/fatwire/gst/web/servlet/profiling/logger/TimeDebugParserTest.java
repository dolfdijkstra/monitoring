package com.fatwire.gst.web.servlet.profiling.logger;

import com.fatwire.gst.web.servlet.profiling.logger.TimeDebugParser;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TimeDebugParserTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParseItPage() throws Exception {
        final int[] count = new int[] { 0 };
        TimeDebugParser p = new TimeDebugParser(
                new TimeDebugParser.ParserCallback() {

                    public void update(String type, String subType, long time) {
                        count[0]++;
                        Assert.assertEquals("page", type);
                        Assert.assertEquals(null, subType);
                        Assert.assertEquals(2696L, time);
                    }

                });
        p.parseIt("Execute time  Hours: 0 Minutes: 0 Seconds: 2:696");
        Assert.assertEquals(1, count[0]);
    }

    public void testParseItPage2() throws Exception {
        final int[] count = new int[] { 0 };
        TimeDebugParser p = new TimeDebugParser(
                new TimeDebugParser.ParserCallback() {

                    public void update(String type, String subType, long time) {
                        count[0]++;
                        Assert.assertEquals("page", type);
                        Assert
                                .assertEquals(
                                        "OpenMarket/Xcelerate/Actions/ShowMyDesktopFront",
                                        subType);
                        Assert.assertEquals(2696L, time);
                    }

                });
        p
                .parseIt("Execute page OpenMarket/Xcelerate/Actions/ShowMyDesktopFront Hours: 0 Minutes: 0 Seconds: 2:696");
        Assert.assertEquals(1, count[0]);
    }

}
