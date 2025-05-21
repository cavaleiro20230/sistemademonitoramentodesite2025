package com.monitor.model;

import java.io.Serializable;

/**
 * Modelo para representar estatísticas de uma URL
 */
public class UrlStat implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String url;
    private int accessCount;
    private double avgResponseTime;
    private int uniqueVisitors;
    private double bounceRate;
    
    // Getters e Setters
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public int getAccessCount() {
        return accessCount;
    }
    
    public void setAccessCount(int accessCount) {
        this.accessCount = accessCount;
    }
    
    public double getAvgResponseTime() {
        return avgResponseTime;
    }
    
    public void setAvgResponseTime(double avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }
    
    public int getUniqueVisitors() {
        return uniqueVisitors;
    }
    
    public void setUniqueVisitors(int uniqueVisitors) {
        this.uniqueVisitors = uniqueVisitors;
    }
    
    public double getBounceRate() {
        return bounceRate;
    }
    
    public void setBounceRate(double bounceRate) {
        this.bounceRate = bounceRate;
    }
    
    /**
     * Formata o tempo médio de resposta em milissegundos para um formato legível
     */
    public String getFormattedAvgResponseTime() {
        long timeMs = (long) avgResponseTime;
        long seconds = timeMs / 1000;
        long ms = timeMs % 1000;
        
        if (seconds > 0) {
            return String.format("%ds %dms", seconds, ms);
        } else {
            return String.format("%dms", ms);
        }
    }
    
    /**
     * Formata a taxa de rejeição como uma porcentagem
     */
    public String getFormattedBounceRate() {
        return String.format("%.1f%%", bounceRate);
    }
    
    @Override
    public String toString() {
        return "UrlStat [url=" + url + ", accessCount=" + accessCount + ", avgResponseTime=" + avgResponseTime
                + ", uniqueVisitors=" + uniqueVisitors + ", bounceRate=" + bounceRate + "]";
    }
}
