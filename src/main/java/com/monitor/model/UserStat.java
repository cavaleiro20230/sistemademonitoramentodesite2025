package com.monitor.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Modelo para representar estatísticas de um usuário
 */
public class UserStat implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String username;
    private int accessCount;
    private Date lastAccess;
    private int uniqueUrls;
    private String totalTime;
    private int sessionCount;
    
    // Getters e Setters
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
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
    
    public String getTotalTime() {
        return totalTime;
    }
    
    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }
    
    public int getSessionCount() {
        return sessionCount;
    }
    
    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }
    
    @Override
    public String toString() {
        return "UserStat [username=" + username + ", accessCount=" + accessCount + ", lastAccess=" + lastAccess
                + ", uniqueUrls=" + uniqueUrls + ", totalTime=" + totalTime + ", sessionCount=" + sessionCount + "]";
    }
}
