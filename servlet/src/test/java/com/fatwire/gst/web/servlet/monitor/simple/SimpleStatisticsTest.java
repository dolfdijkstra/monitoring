package com.fatwire.gst.web.servlet.monitor.simple;

import com.fatwire.gst.web.servlet.monitor.simple.SimpleStatistics;

import junit.framework.TestCase;

public class SimpleStatisticsTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetStandardDeviation() {
        SimpleStatistics s = new SimpleStatistics("test");
        s.addValue(20);
        s.addValue(30);
        s.addValue(40);
        s.addValue(50);
        s.addValue(60);
        assertEquals(15.811388300841896D,s.getStandardDeviation());

    }


}
