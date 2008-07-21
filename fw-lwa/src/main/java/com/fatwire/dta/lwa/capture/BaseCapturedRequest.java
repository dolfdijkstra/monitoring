package com.fatwire.dta.lwa.capture;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public abstract class BaseCapturedRequest {

    private UUID uuid;

    private long timestamp;

    private List<NameValuePair> headers = new LinkedList<NameValuePair>();

    private String method;

    private String queryString;

    private String requestURI;

    private String requestURL;

    private String requestedSessionId;

    private boolean requestedSessionIdValid;

    private String protocol;

    private String remoteAddr;

    private String remoteHost;

    private String serverName;

    private int serverPort;

    private boolean secure;


    public BaseCapturedRequest() {
        super();
    }

    /**
     * @return the headers
     */
    public final Iterable<NameValuePair> getHeaders() {
        return headers;
    }

    /**
     * Add a request header
     * 
     * @param name name of the header
     * @param value of the header
     */
    public final void addHeader(String name, String value) {
        this.headers.add(new NameValuePair(name, value));
    }

    /**
     * @return the secure
     */
    public final boolean isSecure() {
        return secure;
    }

    /**
     * @param secure the secure to set
     */
    public final void setSecure(boolean isSecure) {
        this.secure = isSecure;
    }

    /**
     * @return the method
     */
    public final String getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public final void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return the protocol
     */
    public final String getProtocol() {
        return protocol;
    }

    /**
     * @param protocol the protocol to set
     */
    public final void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return the queryString
     */
    public final String getQueryString() {
        return queryString;
    }

    /**
     * @param queryString the queryString to set
     */
    public final void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    /**
     * @return the remoteAddr
     */
    public final String getRemoteAddr() {
        return remoteAddr;
    }

    /**
     * @param remoteAddr the remoteAddr to set
     */
    public final void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    /**
     * @return the remoteHost
     */
    public final String getRemoteHost() {
        return remoteHost;
    }

    /**
     * @param remoteHost the remoteHost to set
     */
    public final void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    /**
     * @return the requestedSessionId
     */
    public final String getRequestedSessionId() {
        return requestedSessionId;
    }

    /**
     * @param requestedSessionId the requestedSessionId to set
     */
    public final void setRequestedSessionId(String requestedSessionId) {
        this.requestedSessionId = requestedSessionId;
    }

    /**
     * @return the requestURI
     */
    public final String getRequestURI() {
        return requestURI;
    }

    /**
     * @param requestURI the requestURI to set
     */
    public final void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    /**
     * @return the requestURL
     */
    public final String getRequestURL() {
        return requestURL;
    }

    /**
     * @param requestURL the requestURL to set
     */
    public final void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    /**
     * @return the serverName
     */
    public final String getServerName() {
        return serverName;
    }

    /**
     * @param serverName the serverName to set
     */
    public final void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * @return the serverPort
     */
    public final int getServerPort() {
        return serverPort;
    }

    /**
     * @param serverPort the serverPort to set
     */
    public final void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * @return the requestedSessionIdValid
     */
    public final boolean isRequestedSessionIdValid() {
        return requestedSessionIdValid;
    }

    /**
     * @param requestedSessionIdValid the requestedSessionIdValid to set
     */
    public final void setRequestedSessionIdValid(boolean requestedSessionIdValid) {
        this.requestedSessionIdValid = requestedSessionIdValid;
    }

    public final long getTimestamp() {
        return timestamp;
    }

    public final UUID getUUID() {
        return uuid;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public final void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @param uuid the uuid to set
     */
    public final void setUUID(UUID uuid) {
        this.uuid = uuid;
    }


}