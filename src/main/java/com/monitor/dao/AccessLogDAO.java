package com.monitor.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.monitor.model.AccessLog;
import com.monitor.model.UrlStat;
import com.monitor.model.IpStat;
import com.monitor.model.UserStat;
import com.monitor.model.GeoStat;

/**
 * DAO para acessar e manipular os logs de acesso no banco de dados
 */
public class AccessLogDAO {
    
    private static final Logger logger = Logger.getLogger(AccessLogDAO.class);
    private DataSource dataSource;
    
    public AccessLogDAO() {
        try {
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:/jdbc/MonitorDS");
        } catch (NamingException e) {
            logger.error("Erro ao obter DataSource", e);
        }
    }
    
    /**
     * Obtém os logs de acesso com base nos filtros fornecidos
     */
    public List<AccessLog> getLogs(Date startDate, Date endDate, String ipAddress, 
                                  String url, String username, Integer statusCode, 
                                  int offset, int limit) {
        
        List<AccessLog> logs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            StringBuilder sql = new StringBuilder(
                "SELECT id, timestamp, ip_address, url, status_code, " +
                "user_agent, referer, response_time, country, city, username " +
                "FROM access_log WHERE 1=1"
            );
            
            List<Object> params = new ArrayList<>();
            
            // Adiciona filtros à consulta
            if (startDate != null) {
                sql.append(" AND timestamp >= ?");
                params.add(new java.sql.Timestamp(startDate.getTime()));
            }
            
            if (endDate != null) {
                sql.append(" AND timestamp <= ?");
                params.add(new java.sql.Timestamp(endDate.getTime()));
            }
            
            if (ipAddress != null && !ipAddress.isEmpty()) {
                sql.append(" AND ip_address = ?");
                params.add(ipAddress);
            }
            
            if (url != null && !url.isEmpty()) {
                sql.append(" AND url LIKE ?");
                params.add("%" + url + "%");
            }
            
            if (username != null && !username.isEmpty()) {
                sql.append(" AND username = ?");
                params.add(username);
            }
            
            if (statusCode != null) {
                sql.append(" AND status_code = ?");
                params.add(statusCode);
            }
            
            // Adiciona ordenação e paginação
            sql.append(" ORDER BY timestamp DESC LIMIT ? OFFSET ?");
            params.add(limit);
            params.add(offset);
            
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            
            // Define os parâmetros da consulta
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                AccessLog log = new AccessLog();
                log.setId(rs.getLong("id"));
                log.setTimestamp(rs.getTimestamp("timestamp"));
                log.setIpAddress(rs.getString("ip_address"));
                log.setUrl(rs.getString("url"));
                log.setStatusCode(rs.getInt("status_code"));
                log.setUserAgent(rs.getString("user_agent"));
                log.setReferer(rs.getString("referer"));
                log.setResponseTime(rs.getLong("response_time"));
                log.setCountry(rs.getString("country"));
                log.setCity(rs.getString("city"));
                log.setUsername(rs.getString("username"));
                
                logs.add(log);
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao obter logs de acesso", e);
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return logs;
    }
    
    /**
     * Obtém estatísticas de URLs mais acessadas
     */
    public List<UrlStat> getTopUrls(Date startDate, Date endDate, int limit) {
        List<UrlStat> stats = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            StringBuilder sql = new StringBuilder(
                "SELECT url, COUNT(*) as access_count, " +
                "AVG(response_time) as avg_response_time, " +
                "COUNT(DISTINCT ip_address) as unique_visitors " +
                "FROM access_log WHERE 1=1"
            );
            
            List<Object> params = new ArrayList<>();
            
            // Adiciona filtros de data
            if (startDate != null) {
                sql.append(" AND timestamp >= ?");
                params.add(new java.sql.Timestamp(startDate.getTime()));
            }
            
            if (endDate != null) {
                sql.append(" AND timestamp <= ?");
                params.add(new java.sql.Timestamp(endDate.getTime()));
            }
            
            sql.append(" GROUP BY url ORDER BY access_count DESC LIMIT ?");
            params.add(limit);
            
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            
            // Define os parâmetros da consulta
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                UrlStat stat = new UrlStat();
                stat.setUrl(rs.getString("url"));
                stat.setAccessCount(rs.getInt("access_count"));
                stat.setAvgResponseTime(rs.getDouble("avg_response_time"));
                stat.setUniqueVisitors(rs.getInt("unique_visitors"));
                
                // Calcula a taxa de rejeição (em uma consulta separada)
                stat.setBounceRate(calculateBounceRate(stat.getUrl(), startDate, endDate));
                
                stats.add(stat);
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao obter estatísticas de URLs", e);
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return stats;
    }
    
    /**
     * Calcula a taxa de rejeição para uma URL específica
     */
    private double calculateBounceRate(String url, Date startDate, Date endDate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) as single_page_visits FROM (" +
                "  SELECT ip_address, COUNT(DISTINCT url) as url_count" +
                "  FROM access_log" +
                "  WHERE url = ? AND ip_address IN (" +
                "    SELECT DISTINCT ip_address FROM access_log WHERE url = ?"
            );
            
            List<Object> params = new ArrayList<>();
            params.add(url);
            params.add(url);
            
            // Adiciona filtros de data
            if (startDate != null) {
                sql.append(" AND timestamp >= ?");
                params.add(new java.sql.Timestamp(startDate.getTime()));
            }
            
            if (endDate != null) {
                sql.append(" AND timestamp <= ?");
                params.add(new java.sql.Timestamp(endDate.getTime()));
            }
            
            sql.append("  )" +
                "  GROUP BY ip_address" +
                ") as visits " +
                "WHERE url_count = 1");
            
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            
            // Define os parâmetros da consulta
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                int singlePageVisits = rs.getInt("single_page_visits");
                
                // Obtém o total de visitantes únicos para esta URL
                int totalVisitors = getUniqueVisitorsForUrl(url, startDate, endDate);
                
                if (totalVisitors > 0) {
                    return (double) singlePageVisits / totalVisitors * 100.0;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao calcular taxa de rejeição", e);
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return 0.0;
    }
    
    /**
     * Obtém o número de visitantes únicos para uma URL específica
     */
    private int getUniqueVisitorsForUrl(String url, Date startDate, Date endDate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            StringBuilder sql = new StringBuilder(
                "SELECT COUNT(DISTINCT ip_address) as unique_visitors " +
                "FROM access_log WHERE url = ?"
            );
            
            List<Object> params = new ArrayList<>();
            params.add(url);
            
            // Adiciona filtros de data
            if (startDate != null) {
                sql.append(" AND timestamp >= ?");
                params.add(new java.sql.Timestamp(startDate.getTime()));
            }
            
            if (endDate != null) {
                sql.append(" AND timestamp <= ?");
                params.add(new java.sql.Timestamp(endDate.getTime()));
            }
            
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            
            // Define os parâmetros da consulta
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("unique_visitors");
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao obter visitantes únicos", e);
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return 0;
    }
    
    /**
     * Obtém estatísticas de IPs mais ativos
     */
    public List<IpStat> getTopIps(Date startDate, Date endDate, int limit) {
        List<IpStat> stats = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            StringBuilder sql = new StringBuilder(
                "SELECT ip_address, COUNT(*) as access_count, " +
                "MAX(timestamp) as last_access, " +
                "COUNT(DISTINCT url) as unique_urls, " +
                "MAX(country) as country, MAX(city) as city " +
                "FROM access_log WHERE 1=1"
            );
            
            List<Object> params = new ArrayList<>();
            
            // Adiciona filtros de data
            if (startDate != null) {
                sql.append(" AND timestamp >= ?");
                params.add(new java.sql.Timestamp(startDate.getTime()));
            }
            
            if (endDate != null) {
                sql.append(" AND timestamp <= ?");
                params.add(new java.sql.Timestamp(endDate.getTime()));
            }
            
            sql.append(" GROUP BY ip_address ORDER BY access_count DESC LIMIT ?");
            params.add(limit);
            
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            
            // Define os parâmetros da consulta
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                IpStat stat = new IpStat();
                stat.setIpAddress(rs.getString("ip_address"));
                stat.setAccessCount(rs.getInt("access_count"));
                stat.setLastAccess(rs.getTimestamp("last_access"));
                stat.setUniqueUrls(rs.getInt("unique_urls"));
                stat.setCountry(rs.getString("country"));
                stat.setCity(rs.getString("city"));
                
                // Determina o status do IP (normal, suspeito, etc.)
                stat.setStatus(determineIpStatus(stat.getIpAddress(), stat.getAccessCount(), startDate, endDate));
                
                stats.add(stat);
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao obter estatísticas de IPs", e);
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return stats;
    }
    
    /**
     * Determina o status de um IP com base em seu comportamento
     */
    private String determineIpStatus(String ipAddress, int accessCount, Date startDate, Date endDate) {
        // Implementação simplificada - em um sistema real, usaria algoritmos mais sofisticados
        // para detectar comportamentos suspeitos
        
        // Verifica se o IP tem muitos acessos em um curto período de tempo
        if (accessCount > 1000) {
            return "Suspeito";
        }
        
        // Verifica se o IP tentou acessar páginas restritas
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) as error_count " +
                "FROM access_log " +
                "WHERE ip_address = ? AND status_code >= 400"
            );
            
            List<Object> params = new ArrayList<>();
            params.add(ipAddress);
            
            // Adiciona filtros de data
            if (startDate != null) {
                sql.append(" AND timestamp >= ?");
                params.add(new java.sql.Timestamp(startDate.getTime()));
            }
            
            if (endDate != null) {
                sql.append(" AND timestamp <= ?");
                params.add(new java.sql.Timestamp(endDate.getTime()));
            }
            
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            
            // Define os parâmetros da consulta
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                int errorCount = rs.getInt("error_count");
                if (errorCount > 50) {
                    return "Suspeito";
                }
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao determinar status do IP", e);
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return "Normal";
    }
    
    /**
     * Obtém estatísticas de usuários mais ativos
     */
    public List<UserStat> getTopUsers(Date startDate, Date endDate, int limit) {
        List<UserStat> stats = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            StringBuilder sql = new StringBuilder(
                "SELECT username, COUNT(*) as access_count, " +
                "MAX(timestamp) as last_access, " +
                "COUNT(DISTINCT url) as unique_urls, " +
                "SUM(response_time) as total_time " +
                "FROM access_log " +
                "WHERE username IS NOT NULL AND username != ''"
            );
            
            List<Object> params = new ArrayList<>();
            
            // Adiciona filtros de data
            if (startDate != null) {
                sql.append(" AND timestamp >= ?");
                params.add(new java.sql.Timestamp(startDate.getTime()));
            }
            
            if (endDate != null) {
                sql.append(" AND timestamp <= ?");
                params.add(new java.sql.Timestamp(endDate.getTime()));
            }
            
            sql.append(" GROUP BY username ORDER BY access_count DESC LIMIT ?");
            params.add(limit);
            
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            
            // Define os parâmetros da consulta
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                UserStat stat = new UserStat();
                stat.setUsername(rs.getString("username"));
                stat.setAccessCount(rs.getInt("access_count"));
                stat.setLastAccess(rs.getTimestamp("last_access"));
                stat.setUniqueUrls(rs.getInt("unique_urls"));
                
                // Converte o tempo total de milissegundos para um formato mais legível
                long totalTimeMs = rs.getLong("total_time");
                stat.setTotalTime(formatTime(totalTimeMs));
                
                // Obtém o número de sessões do usuário
                stat.setSessionCount(getUserSessionCount(stat.getUsername(), startDate, endDate));
                
                stats.add(stat);
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao obter estatísticas de usuários", e);
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return stats;
    }
    
    /**
     * Obtém o número de sessões de um usuário
     */
    private int getUserSessionCount(String username, Date startDate, Date endDate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            // Uma sessão é definida como um conjunto de acessos com intervalo menor que 30 minutos
            StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) as session_count FROM (" +
                "  SELECT COUNT(*) FROM (" +
                "    SELECT timestamp, " +
                "      CASE " +
                "        WHEN DATEDIFF(MINUTE, LAG(timestamp) OVER (ORDER BY timestamp), timestamp) > 30 " +
                "        THEN 1 ELSE 0 " +
                "      END as new_session " +
                "    FROM access_log " +
                "    WHERE username = ?"
            );
            
            List<Object> params = new ArrayList<>();
            params.add(username);
            
            // Adiciona filtros de data
            if (startDate != null) {
                sql.append(" AND timestamp >= ?");
                params.add(new java.sql.Timestamp(startDate.getTime()));
            }
            
            if (endDate != null) {
                sql.append(" AND timestamp <= ?");
                params.add(new java.sql.Timestamp(endDate.getTime()));
            }
            
            sql.append("  ) as sessions " +
                "  WHERE new_session = 1 OR new_session IS NULL" +
                ") as session_count");
            
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            
            // Define os parâmetros da consulta
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("session_count");
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao obter contagem de sessões do usuário", e);
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return 0;
    }
    
    /**
     * Obtém estatísticas de geolocalização
     */
    public Map<String, List<GeoStat>> getGeoStats(Date startDate, Date endDate) {
        Map<String, List<GeoStat>> result = new HashMap<>();
        
        // Obtém estatísticas por país
        result.put("countries", getGeoStatsByField("country", startDate, endDate, 10));
        
        // Obtém estatísticas por cidade
        result.put("cities", getGeoStatsByField("city", startDate, endDate, 10));
        
        return result;
    }
    
    /**
     * Obtém estatísticas de geolocalização por campo específico (país ou cidade)
     */
    private List<GeoStat> getGeoStatsByField(String field, Date startDate, Date endDate, int limit) {
        List<GeoStat> stats = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            StringBuilder sql = new StringBuilder(
                "SELECT " + field + ", COUNT(*) as access_count, " +
                "COUNT(DISTINCT ip_address) as unique_visitors " +
                "FROM access_log " +
                "WHERE " + field + " IS NOT NULL AND " + field + " != ''"
            );
            
            List<Object> params = new ArrayList<>();
            
            // Adiciona filtros de data
            if (startDate != null) {
                sql.append(" AND timestamp >= ?");
                params.add(new java.sql.Timestamp(startDate.getTime()));
            }
            
            if (endDate != null) {
                sql.append(" AND timestamp <= ?");
                params.add(new java.sql.Timestamp(endDate.getTime()));
            }
            
            sql.append(" GROUP BY " + field + " ORDER BY access_count DESC LIMIT ?");
            params.add(limit);
            
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            
            // Define os parâmetros da consulta
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                GeoStat stat = new GeoStat();
                stat.setLocation(rs.getString(field));
                stat.setAccessCount(rs.getInt("access_count"));
                stat.setUniqueVisitors(rs.getInt("unique_visitors"));
                
                stats.add(stat);
            }
            
        } catch (SQLException e) {
            logger.error("Erro ao obter estatísticas de geolocalização", e);
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return stats;
    }
    
    /**
     * Formata o tempo em milissegundos para um formato legível (horas, minutos, segundos)
     */
    private String formatTime(long timeMs) {
        long seconds = timeMs / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        minutes = minutes % 60;
        seconds = seconds % 60;
        
        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        } else {
            return String.format("%ds", seconds);
        }
    }
    
    /**
     * Fecha recursos JDBC
     */
    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Erro ao fechar ResultSet", e);
            }
        }
        
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.error("Erro ao fechar PreparedStatement", e);
            }
        }
        
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Erro ao fechar Connection", e);
            }
        }
    }
}
