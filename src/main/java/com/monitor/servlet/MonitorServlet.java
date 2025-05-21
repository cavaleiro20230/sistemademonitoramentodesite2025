package com.monitor.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.monitor.model.AccessLog;
import com.monitor.model.GeoStat;
import com.monitor.model.IpStat;
import com.monitor.model.UrlStat;
import com.monitor.model.UserStat;
import com.monitor.service.MonitorService;

/**
 * Servlet para exibir o painel de monitoramento
 */
public class MonitorServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(MonitorServlet.class);
    
    private MonitorService monitorService;
    
    @Override
    public void init() throws ServletException {
        monitorService = new MonitorService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Obtém parâmetros de filtro
            Date startDate = parseDate(request.getParameter("startDate"));
            Date endDate = parseDate(request.getParameter("endDate"));
            
            // Se não foram fornecidas datas, usa o último dia
            if (startDate == null) {
                startDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
            }
            if (endDate == null) {
                endDate = new Date();
            }
            
            // Obtém estatísticas gerais
            int totalVisits = monitorService.getTotalVisits(startDate, endDate);
            int uniqueVisitors = monitorService.getUniqueVisitors(startDate, endDate);
            
            // Obtém estatísticas detalhadas
            List<UrlStat> topUrls = monitorService.getTopUrls(startDate, endDate, 10);
            List<IpStat> topIps = monitorService.getTopIps(startDate, endDate, 10);
            List<UserStat> topUsers = monitorService.getTopUsers(startDate, endDate, 10);
            Map<String, List<GeoStat>> geoStats = monitorService.getGeoStats(startDate, endDate);
            
            // Obtém logs recentes
            List<AccessLog> recentLogs = monitorService.getLogs(startDate, endDate, null, null, null, null, 0, 20);
            
            // Define atributos para a página JSP
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.setAttribute("totalVisits", totalVisits);
            request.setAttribute("uniqueVisitors", uniqueVisitors);
            request.setAttribute("topUrls", topUrls);
            request.setAttribute("topIps", topIps);
            request.setAttribute("topUsers", topUsers);
            request.setAttribute("geoStats", geoStats);
            request.setAttribute("recentLogs", recentLogs);
            
            // Encaminha para a página JSP
            request.getRequestDispatcher("/WEB-INF/jsp/monitor.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Erro ao processar requisição", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar requisição: " + e.getMessage());
        }
    }
    
    /**
     * Converte uma string de data para um objeto Date
     */
    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            logger.warn("Erro ao converter data: " + dateStr, e);
            return null;
        }
    }
}
