package com.monitor.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.monitor.dao.AccessLogDAO;
import com.monitor.model.AccessLog;
import com.monitor.model.GeoStat;
import com.monitor.model.IpStat;
import com.monitor.model.UrlStat;
import com.monitor.model.UserStat;

/**
 * Serviço para obter estatísticas e logs de monitoramento
 */
public class MonitorService {
    
    private AccessLogDAO accessLogDAO;
    
    public MonitorService() {
        accessLogDAO = new AccessLogDAO();
    }
    
    /**
     * Obtém logs de acesso com base nos filtros fornecidos
     */
    public List<AccessLog> getLogs(Date startDate, Date endDate, String ipAddress, 
                                  String url, String username, Integer statusCode, 
                                  int offset, int limit) {
        return accessLogDAO.getLogs(startDate, endDate, ipAddress, url, username, statusCode, offset, limit);
    }
    
    /**
     * Obtém estatísticas de URLs mais acessadas
     */
    public List<UrlStat> getTopUrls(Date startDate, Date endDate, int limit) {
        return accessLogDAO.getTopUrls(startDate, endDate, limit);
    }
    
    /**
     * Obtém estatísticas de IPs mais ativos
     */
    public List<IpStat> getTopIps(Date startDate, Date endDate, int limit) {
        return accessLogDAO.getTopIps(startDate, endDate, limit);
    }
    
    /**
     * Obtém estatísticas de usuários mais ativos
     */
    public List<UserStat> getTopUsers(Date startDate, Date endDate, int limit) {
        return accessLogDAO.getTopUsers(startDate, endDate, limit);
    }
    
    /**
     * Obtém estatísticas de geolocalização
     */
    public Map<String, List<GeoStat>> getGeoStats(Date startDate, Date endDate) {
        return accessLogDAO.getGeoStats(startDate, endDate);
    }
    
    /**
     * Obtém o total de visitas no período
     */
    public int getTotalVisits(Date startDate, Date endDate) {
        // Implementação simplificada - em um sistema real, teria uma consulta específica para isso
        List<AccessLog> logs = accessLogDAO.getLogs(startDate, endDate, null, null, null, null, 0, 1);
        return logs.size();
    }
    
    /**
     * Obtém o total de visitantes únicos no período
     */
    public int getUniqueVisitors(Date startDate, Date endDate) {
        // Implementação simplificada - em um sistema real, teria uma consulta específica para isso
        List<IpStat> ips = accessLogDAO.getTopIps(startDate, endDate, Integer.MAX_VALUE);
        return ips.size();
    }
}
