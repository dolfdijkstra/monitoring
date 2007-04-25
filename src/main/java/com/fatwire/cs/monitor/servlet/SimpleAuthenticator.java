package com.fatwire.cs.monitor.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SimpleAuthenticator implements Authenticator {
    private final Log log = LogFactory.getLog(getClass());

    private final Map<String, String> users = new HashMap<String, String>();

    public void init(final ServletConfig config) throws ServletException {
        users.put("fwadmin:xceladmin", "allowed");
    }

    public boolean authenticate(final HttpServletRequest req,
            final HttpServletResponse res) throws ServletException, IOException {

        // Get Authorization header
        final String auth = req.getHeader("Authorization");
        log.info(auth);

        // Do we allow that user?
        if (!allowUser(auth)) {
            // Not allowed, so report he's unauthorized
            res.setHeader("WWW-Authenticate", "BASIC realm=\"admin\"");
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        } else {
            return true;
        }
    }

    // This method checks the user information sent in the Authorization
    // header against the database of users maintained in the users Hashtable.
    protected boolean allowUser(final String auth) throws IOException {
        if (auth == null) {
            return false; // no auth
        }

        if (!auth.toUpperCase().startsWith("BASIC ")) {
            return false; // we only do BASIC
        }

        // Get encoded user and password, comes after "BASIC "
        final String userpassEncoded = auth.substring(6);

        // Decode it, using any base 64 decoder
        final Base64 dec = new Base64();
        final byte[] userpassDecoded = dec.decode(userpassEncoded
                .getBytes("ISO-8859_1"));

        // Check our user list to see if that user and password are "allowed"
        return "allowed".equals(users.get(new String(userpassDecoded,
                "ISO-8859-1")));
    }
}
