package com.fatwire.gst.web.servlet.profiling;

public class Hierarchy {
    private final String name;

    private final Hierarchy parent;

    public Hierarchy(String name) {
        this(name,null);
    }
    private Hierarchy(String name, Hierarchy parent) {
        super();
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("name can not be null or empty");
        this.name = name;
        this.parent = parent;
    }

    public Hierarchy build(String name) {
        return new Hierarchy(name, this);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the parent
     */
    public Hierarchy getParent() {
        return parent;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Hierarchy)) {
            return false;
        }
        Hierarchy other = (Hierarchy) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (parent == null) {
            if (other.parent != null) {
                return false;
            }
        } else if (!parent.equals(other.parent)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (parent == null) {
            return name;
        } else {
            return parent.toString() + "." + name;
        }

    }

}
