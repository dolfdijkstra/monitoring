package com.fatwire.gst.web.servlet.profiling.servlet.jmx;

import com.fatwire.gst.web.servlet.profiling.servlet.jmx.NameBuilder;

import junit.framework.TestCase;

public class NameBuilderTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSanitize() {
        NameBuilder b = new NameBuilder();
        String x = b.sanitize("foo/bar\"boo\"while,now");
        
        assertEquals("foo/bar_boo_while_now",x);
    }

}
