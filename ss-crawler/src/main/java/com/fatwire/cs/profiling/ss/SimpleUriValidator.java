package com.fatwire.cs.profiling.ss;

import com.fatwire.cs.profiling.ss.util.HelperStrings;

public class SimpleUriValidator implements UriValidator {

    private String domain;

    private String domainWithCS;

    public boolean validate(String uri) {
        return this.validateForDomainStart(uri)
                && this.validateForPageDataRequest(uri);
    }

    protected boolean validateForDomainStart(String uri) {
        return !uri.startsWith(domainWithCS);
    }

    protected boolean validateForPageDataRequest(String uri) {
        return uri.indexOf(HelperStrings.SS_PAGEDATA_REQUEST) < 1;
    }

    /**
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
        this.domainWithCS = domain + HelperStrings.CONTENTSERVER;
    }

}
