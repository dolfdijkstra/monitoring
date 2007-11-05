package com.fatwire.cs.profiling.ss.events;



import com.fatwire.cs.profiling.ss.ResultPage;

public class PageletRenderedEvent extends EventObject {
    private final ResultPage page;

    public PageletRenderedEvent(Object source, ResultPage page) {
        super(source);
        this.page = page;
    }

    /**
     * 
     */
    private static final long serialVersionUID = 87405192630120962L;

    /**
     * @return the page
     */
    public ResultPage getPage() {
        return page;
    }

}
