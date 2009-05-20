package com.fatwire.cs.profiling.version;

public interface ProductInfoMBean {
    
    
    public String getProductJar();

    /**
     * @return the productName
     */
    public String getProductName();

    /**
     * @return the productVersion
     */
    public String getProductVersionDescription();

    public String getVersion();
    /**
     * @return the productVersionInfoClass
     */
    public String getProductVersionInfoClass();

}
