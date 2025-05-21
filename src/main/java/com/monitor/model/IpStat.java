package com.monitor.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Modelo para representar estatísticas de um endereço IP
 */
public class IpStat implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String ipAddress;
    private int accessCount;
    private Date lastAccess;
    private int uniqueUrls;
    private String country;
    private String city;
    private String status;
    
    // Getters e Setters
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public int getAccessCount() {
        return accessCount;
    }
    
    public void setAccessCount(int accessCount) {
        this.accessCount = accessCount;
    }
    
    public Date getLastAccess() {
        return lastAccess;
    }
    
    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }
    
    public int getUniqueUrls() {
        return uniqueUrls;
    }
    
    public void setUniqueUrls(int uniqueUrls) {
        this.uniqueUrls = uniqueUrls;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Retorna a localização completa (cidade, país)
     */
    public String getLocation() {
        if (city != null && !city.isEmpty() && country != null && !country.isEmpty()) {
            return city + ", " + country;
        } else if (country != null && !country.isEmpty()) {
            return country;
        } else {
            return "Desconhecido";
        }
    }
    
    @Override
    public String toString() {
        return "IpStat [ipAddress=" + ipAddress + ", accessCount=" + accessCount + ", lastAccess=" + lastAccess
                + ", uniqueUrls=" + uniqueUrls + ", country=" + country + ", city=" + city + ", status=" + status + "]";
    }
}
