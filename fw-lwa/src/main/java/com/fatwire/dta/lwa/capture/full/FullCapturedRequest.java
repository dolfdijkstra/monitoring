package com.fatwire.dta.lwa.capture.full;

import java.io.Serializable;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;

import com.fatwire.dta.lwa.capture.BaseCapturedRequest;
import com.fatwire.dta.lwa.capture.CapturedRequest;
import com.fatwire.dta.lwa.capture.NameValuePair;
import com.fatwire.dta.lwa.capture.UserPrincipal;

/**
 * @author Dolf.Dijkstra
 * @since Jan 7, 2008
 */
public class FullCapturedRequest extends BaseCapturedRequest implements
        Serializable, CapturedRequest {

    /**
     * 
     */
    private static final long serialVersionUID = 1868953052993101195L;

    private String authType;

    private String contextPath;

    private Cookie[] cookies;

    private String pathInfo;

    private String pathTranslated;

    private String remoteUser;

    private String servletPath;

    private Principal userPrincipal;

    private String characterEncoding;

    private int contentLength;

    private String contentType;

    private Locale locale;

    private List<Locale> locales;

    private List<NameValuePair> parameters = new LinkedList<NameValuePair>();

    private String scheme;

    /**
     * 
     */
    public FullCapturedRequest() {
        super();
    }

    /**
     * @return the authType
     */
    public String getAuthType() {
        return authType;
    }

    /**
     * @param authType the authType to set
     */
    public void setAuthType(String authType) {
        this.authType = authType;
    }

    /**
     * @return the characterEncoding
     */
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    /**
     * @param characterEncoding the characterEncoding to set
     */
    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    /**
     * @return the contentLength
     */
    public int getContentLength() {
        return contentLength;
    }

    /**
     * @param contentLength the contentLength to set
     */
    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the contextPath
     */
    public String getContextPath() {
        return contextPath;
    }

    /**
     * @param contextPath the contextPath to set
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * @return the cookies
     */
    public Cookie[] getCookies() {
        return cookies;
    }

    /**
     * @param cookies the cookies to set
     */
    public void setCookies(Cookie[] cookies) {
        this.cookies = cookies;
    }

    /**
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * @return the locales
     */
    public Iterable<Locale> getLocales() {
        return locales;
    }

    /**
     * @param locale locale to add
     */
    public void addLocale(Locale locale) {
        this.locales.add(locale);
    }

    /**
     * @return the parameters
     */
    public Iterable<NameValuePair> getParameters() {
        return parameters;
    }

    /**
     * @param name
     * @param value
     */
    public void addParameter(String name, String value) {
        this.parameters.add(new NameValuePair(name, value));
    }

    /**
     * @return the pathInfo
     */
    public String getPathInfo() {
        return pathInfo;
    }

    /**
     * @param pathInfo the pathInfo to set
     */
    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    /**
     * @return the pathTranslated
     */
    public String getPathTranslated() {
        return pathTranslated;
    }

    /**
     * @param pathTranslated the pathTranslated to set
     */
    public void setPathTranslated(String pathTranslated) {
        this.pathTranslated = pathTranslated;
    }

    /**
     * @return the remoteUser
     */
    public String getRemoteUser() {
        return remoteUser;
    }

    /**
     * @param remoteUser the remoteUser to set
     */
    public void setRemoteUser(String remoteUser) {
        this.remoteUser = remoteUser;
    }

    /**
     * @return the scheme
     */
    public String getScheme() {
        return scheme;
    }

    /**
     * @param scheme the scheme to set
     */
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * @return the servletPath
     */
    public String getServletPath() {
        return servletPath;
    }

    /**
     * @param servletPath the servletPath to set
     */
    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    /**
     * @return the userPrincipal
     */
    public Principal getUserPrincipal() {
        return userPrincipal;
    }

    /**
     * @param userPrincipal the userPrincipal to set
     */
    public void setUserPrincipal(Principal userPrincipal) {
        this.userPrincipal = userPrincipal == null ? null : new UserPrincipal(
                userPrincipal.getName());
    }

}
