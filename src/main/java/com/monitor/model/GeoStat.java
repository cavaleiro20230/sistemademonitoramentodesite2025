package com.monitor.model;

import java.io.Serializable;

/**
 * Modelo para representar estatísticas de geolocalização
 */
public class GeoStat implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String location;
    private int accessCount;
    private int uniqueVisitors;
    
    // Getters e Setters
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public int getAccessCount() {
        return accessCount;
    }
    
    public void setAccessCount(int accessCount) {
        this.accessCount = accessCount;
    }
    
    public int getUniqueVisitors() {
        return uniqueVisitors;
    }
    
    public void setUniqueVisitors(int uniqueVisitors) {
        this.uniqueVisitors = uniqueVisitors;
    }
    
    @Override
    public String toString() {
        return "GeoStat [location=" + location + ", accessCount=" + accessCount + ", uniqueVisitors=" + uniqueVisitors
                + "]";
    }
}
