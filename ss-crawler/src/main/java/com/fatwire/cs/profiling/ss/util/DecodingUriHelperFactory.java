package com.fatwire.cs.profiling.ss.util;

public class DecodingUriHelperFactory extends UriHelperFactory {

    public SSUriHelper create(String path) {
        return new DecodingSSUriHelper(path);
    }

}
