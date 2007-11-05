package com.fatwire.cs.profiling.ss;

import java.util.Map;
import java.util.TreeMap;

public class SSUri {

    private final Map<String, String> map = new TreeMap<String, String>();

    public void addParameter(String key, String value) {
        this.map.put(key, value);
    }

    public Map<String, String> getParameters() {
        return map;
    }

    public void clear() {
        map.clear();
    }
    
    public boolean isOK(){
        return !map.isEmpty();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((map == null) ? 0 : map.hashCode());
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
        final SSUri other = (SSUri) obj;
        if (map == null) {
            if (other.map != null)
                return false;
        } else if (!map.equals(other.map))
            return false;
        return true;
    }

}
