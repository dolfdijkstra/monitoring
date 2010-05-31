package com.fatwire.gst.web.servlet.profiling.servlet.filter;

import com.fatwire.gst.web.servlet.profiling.servlet.filter.ExpiresFilter;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ExpiresFilterTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParse15DPeriod() {
        ExpiresFilter f = new ExpiresFilter();
        int p = f.parsePeriod("15d", 60);
        assertEquals(15*60*60*24,p);
    }
    public void testParse120Period() {
        ExpiresFilter f = new ExpiresFilter();
        try {
            int p = f.parsePeriod("120", 60);
            Assert.fail("Should not get here " +p);
        } catch (Exception e) {

        }
    }

    
    public void testParse5mPeriod() {
        ExpiresFilter f = new ExpiresFilter();
        int p = f.parsePeriod("5m", 60);
        assertEquals(5*60,p);
    }
    public void testParseBlankPeriod() {
        ExpiresFilter f = new ExpiresFilter();
        
        try {
            int p = f.parsePeriod("", 60);
            Assert.fail("Should not get here "+p);
        } catch (Exception e) {

        }
        
    }
    public void testParseSingleSpacePeriod() {
        ExpiresFilter f = new ExpiresFilter();
        try {
            int p = f.parsePeriod(" ", 60);
            Assert.fail("Should not get here "+p);
        } catch (Exception e) {

        }
    }

}
