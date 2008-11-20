package com.fatwire.cs.page;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public interface PageFactory<T> {

    Page<T> createPage(final HttpServletResponse response) throws IOException;

}
