package com.fatwire.dta.lwa.capture;

public final class NameValuePair {

    private final String name;
    private final String value;
    /**
     * @param name
     * @param value
     */
    public NameValuePair(final String name, final String value) {
        super();
        this.name = name;
        this.value = value;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((name == null) ? 0 : name.hashCode());
        result = PRIME * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final NameValuePair other = (NameValuePair) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuilder(name).append("=").append(value).toString();
    }
    
}
