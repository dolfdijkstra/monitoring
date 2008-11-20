package com.fatwire.cs.page;

import javax.servlet.http.HttpServletRequest;

public interface ModelFactory<T> {

    T createModel(HttpServletRequest request);
}
