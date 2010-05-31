package com.fatwire.gst.web.servlet.monitor.event;


public interface FetchEventListener extends PerformanceEventListener {

    void fetchPerformed(FetchEvent event);

}
