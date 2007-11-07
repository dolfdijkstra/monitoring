package com.fatwire.cs.profiling.ss.reporting;

import com.fatwire.cs.profiling.ss.ResultPage;

public interface Reporter {

    void startCollecting();
    
    void endCollecting();
    
    void addToReport(ResultPage page);
}
