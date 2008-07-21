package com.fatwire.dta.lwa.capture;

import java.security.Principal;

public class UserPrincipal implements Principal {

    private final String name;

    /**
     * @param name
     */
    public UserPrincipal(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Principal) {
            Principal  p =(Principal)obj;
            return name.equals(p.getName());
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return name.toString();
    }

}
