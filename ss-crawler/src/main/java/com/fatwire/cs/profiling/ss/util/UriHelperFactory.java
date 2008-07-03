package com.fatwire.cs.profiling.ss.util;

public class UriHelperFactory {

    public SSUriHelper create(String path) {
        return new SSUriHelper(path);
    }

}
