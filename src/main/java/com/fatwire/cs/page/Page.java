package com.fatwire.cs.page;

public interface Page<T> {

    public void renderPage(T model) throws Exception;


}