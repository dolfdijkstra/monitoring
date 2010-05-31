package com.fatwire.gst.web.servlet.profiling.servlet.jmx;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

public class NameBuilder {
    private final String[] parameters = new String[] { "pagename", "blobtable" };
    String sanitize(String s) {
        return s.replaceAll("[,=:\"*?]", "_");
    }

    String extractName(HttpServletRequest request) {

        StringBuilder b = new StringBuilder(",path=").append(request
                .getRequestURI());
        //        if (request.getQueryString() != null) {
        //            for (String part : request.getQueryString().split("&")) {
        //                if (part.startsWith("pagename=")) {
        //                    b.append(',');
        //                    try {
        //                        b.append(java.net.URLDecoder.decode(part, "UTF-8"));
        //                    } catch (UnsupportedEncodingException e) {
        //                        b.append(part);
        //                    }
        //                } else if (part.startsWith("blobtable=")) {
        //                    b.append(',').append(part);
        //                }
        //            }
        //
        //        }

        for (String a : parameters) {
            if (request.getParameter(a) != null) {
                b.append(',');
                b.append(a);
                b.append('=');
                try {
                    b.append(java.net.URLDecoder.decode(
                            request.getParameter(a), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    b.append(request.getParameter(a));
                }
                break;
            }
        }
        return b.toString();
    }

}
