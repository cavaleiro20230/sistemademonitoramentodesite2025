package com.monitor.filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.monitor.util.GeoIPLookup;

/**
 * Servlet Filter para capturar todas as requisições HTTP
 * e registrar informações de acesso no banco de dados.
 */
public class RequestLogFilter implements Filter {
    
    private static final Logger logger = Logger.getLogger(RequestLogFilter.class);
    private DataSource dataSource;
    private GeoIPLookup geoIPLookup;
    
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            // Obtém o DataSource configurado no JBOSS
            InitialContext ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:/jdbc/MonitorDS");
            
            // Inicializa o serviço de geolocalização
            geoIPLookup = new GeoIPLookup();
            geoIPLookup.init();
            
            logger.info("RequestLogFilter inicializado com sucesso");
        } catch (NamingException e) {
            logger.error("Erro ao obter DataSource", e);
            throw new ServletException("Erro ao inicializar o filtro de log", e);
        } catch (Exception e) {
            logger.error("Erro ao inicializar GeoIPLookup", e);
            throw new ServletException("Erro ao inicializar o serviço de geolocalização", e);
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        long startTime = System.currentTimeMillis();
        
        // Continua a cadeia de filtros
        chain.doFilter(request, response);
        
        // Calcula o tempo de resposta
        long responseTime = System.currentTimeMillis() - startTime;
        
        // Registra a requisição após o processamento
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            logRequest((HttpServletRequest) request, (HttpServletResponse) response, responseTime);
        }
    }
    
    private void logRequest(HttpServletRequest request, HttpServletResponse response, long responseTime) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            // Obtém informações da requisição
            String ipAddress = getClientIpAddress(request);
            String url = request.getRequestURI();
            String queryString = request.getQueryString();
            if (queryString != null) {
                url += "?" + queryString;
            }
            
            int statusCode = response.getStatus();
            String userAgent = request.getHeader("User-Agent");
            String referer = request.getHeader("Referer");
            
            // Obtém informações de geolocalização
            String country = null;
            String city = null;
            try {
                country = geoIPLookup.getCountry(ipAddress);
                city = geoIPLookup.getCity(ipAddress);
            } catch (Exception e) {
                logger.warn("Erro ao obter geolocalização para IP: " + ipAddress, e);
            }
            
            // Obtém informações de usuário autenticado (se houver)
            String username = null;
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object user = session.getAttribute("user");
                if (user != null) {
                    username = user.toString();
                }
            }
            
            // Insere o log no banco de dados
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(
                "INSERT INTO access_log (timestamp, ip_address, url, status_code, " +
                "user_agent, referer, response_time, country, city, username) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            
            stmt.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()));
            stmt.setString(2, ipAddress);
            stmt.setString(3, url);
            stmt.setInt(4, statusCode);
            stmt.setString(5, userAgent);
            stmt.setString(6, referer);
            stmt.setLong(7, responseTime);
            stmt.setString(8, country);
            stmt.setString(9, city);
            stmt.setString(10, username);
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            logger.error("Erro ao registrar log de acesso", e);
        } finally {
            // Fecha recursos
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
    
    /**
     * Obtém o endereço IP real do cliente, considerando proxies
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // Se for uma lista de IPs (X-Forwarded-For pode conter múltiplos IPs), pega o primeiro
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }

    public void destroy() {
        // Libera recursos
        if (geoIPLookup != null) {
            geoIPLookup.close();
        }
        logger.info("RequestLogFilter destruído");
    }
}
