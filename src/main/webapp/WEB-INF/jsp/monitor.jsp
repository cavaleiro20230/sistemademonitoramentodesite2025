<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Painel de Monitoramento</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        .header h1 {
            margin: 0;
            color: #333;
        }
        .filter-form {
            background-color: #fff;
            padding: 15px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .filter-form label {
            margin-right: 10px;
        }
        .filter-form input[type="date"] {
            padding: 5px;
            border: 1px solid #ddd;
            border-radius: 3px;
        }
        .filter-form button {
            padding: 5px 15px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
        .filter-form button:hover {
            background-color: #45a049;
        }
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 20px;
        }
        .stat-card {
            background-color: #fff;
            padding: 15px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        .stat-card h3 {
            margin-top: 0;
            color: #555;
            font-size: 16px;
        }
        .stat-card .value {
            font-size: 24px;
            font-weight: bold;
            color: #333;
        }
        .tabs {
            display: flex;
            border-bottom: 1px solid #ddd;
            margin-bottom: 20px;
        }
        .tab {
            padding: 10px 20px;
            cursor: pointer;
            border: 1px solid transparent;
            border-bottom: none;
            border-radius: 5px 5px 0 0;
            margin-right: 5px;
        }
        .tab.active {
            background-color: #fff;
            border-color: #ddd;
            font-weight: bold;
        }
        .tab-content {
            display: none;
            background-color: #fff;
            padding: 20px;
            border-radius: 0 0 5px 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        .tab-content.active {
            display: block;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        table th, table td {
            padding: 10px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        table th {
            background-color: #f5f5f5;
            font-weight: bold;
        }
        .status {
            display: inline-block;
            padding: 3px 8px;
            border-radius: 12px;
            font-size: 12px;
        }
        .status-normal {
            background-color: #e8f5e9;
            color: #2e7d32;
        }
        .status-warning {
            background-color: #fff8e1;
            color: #f57f17;
        }
        .status-error {
            background-color: #ffebee;
            color: #c62828;
        }
        .geo-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
        @media (max-width: 768px) {
            .stats-grid {
                grid-template-columns: 1fr;
            }
            .geo-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Painel de Monitoramento</h1>
        </div>
        
        <div class="filter-form">
            <form action="monitor" method="get">
                <label for="startDate">Data Inicial:</label>
                <input type="date" id="startDate" name="startDate" value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd" />">
                
                <label for="endDate">Data Final:</label>
                <input type="date" id="endDate" name="endDate" value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd" />">
                
                <button type="submit">Filtrar</button>
            </form>
        </div>
        
        <div class="stats-grid">
            <div class="stat-card">
                <h3>Total de Visitas</h3>
                <div class="value">${totalVisits}</div>
            </div>
            <div class="stat-card">
                <h3>Visitantes Únicos</h3>
                <div class="value">${uniqueVisitors}</div>
            </div>
            <div class="stat-card">
                <h3>Páginas por Visita</h3>
                <div class="value">
                    <c:if test="${uniqueVisitors > 0}">
                        <fmt:formatNumber value="${totalVisits / uniqueVisitors}" maxFractionDigits="1" />
                    </c:if>
                    <c:if test="${uniqueVisitors == 0}">0</c:if>
                </div>
            </div>
            <div class="stat-card">
                <h3>Período</h3>
                <div class="value" style="font-size: 16px;">
                    <fmt:formatDate value="${startDate}" pattern="dd/MM/yyyy" /> - 
                    <fmt:formatDate value="${endDate}" pattern="dd/MM/yyyy" />
                </div>
            </div>
        </div>
        
        <div class="tabs">
            <div class="tab active" onclick="openTab(event, 'urls')">URLs Mais Acessadas</div>
            <div class="tab" onclick="openTab(event, 'ips')">IPs</div>
            <div class="tab" onclick="openTab(event, 'users')">Usuários</div>
            <div class="tab" onclick="openTab(event, 'geo')">Geolocalização</div>
            <div class="tab" onclick="openTab(event, 'logs')">Logs</div>
        </div>
        
        <div id="urls" class="tab-content active">
            <h2>URLs Mais Acessadas</h2>
            <table>
                <thead>
                    <tr>
                        <th>URL</th>
                        <th>Acessos</th>
                        <th>Tempo Médio</th>
                        <th>Visitantes Únicos</th>
                        <th>Taxa de Rejeição</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${topUrls}" var="url">
                        <tr>
                            <td>${url.url}</td>
                            <td>${url.accessCount}</td>
                            <td>${url.formattedAvgResponseTime}</td>
                            <td>${url.uniqueVisitors}</td>
                            <td>${url.formattedBounceRate}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty topUrls}">
                        <tr>
                            <td colspan="5" style="text-align: center;">Nenhum dado encontrado</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
        
        <div id="ips" class="tab-content">
            <h2>IPs de Acesso</h2>
            <table>
                <thead>
                    <tr>
                        <th>Endereço IP</th>
                        <th>Acessos</th>
                        <th>Último Acesso</th>
                        <th>Localização</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${topIps}" var="ip">
                        <tr>
                            <td>${ip.ipAddress}</td>
                            <td>${ip.accessCount}</td>
                            <td><fmt:formatDate value="${ip.lastAccess}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                            <td>${ip.location}</td>
                            <td>
                                <span class="status ${ip.status eq 'Normal' ? 'status-normal' : 'status-warning'}">
                                    ${ip.status}
                                </span>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty topIps}">
                        <tr>
                            <td colspan="5" style="text-align: center;">Nenhum dado encontrado</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
        
        <div id="users" class="tab-content">
            <h2>Usuários Ativos</h2>
            <table>
                <thead>
                    <tr>
                        <th>Usuário</th>
                        <th>Acessos</th>
                        <th>Sessões</th>
                        <th>Última Atividade</th>
                        <th>Tempo Total</th>
                        <th>Páginas Visitadas</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${topUsers}" var="user">
                        <tr>
                            <td>${user.username}</td>
                            <td>${user.accessCount}</td>
                            <td>${user.sessionCount}</td>
                            <td><fmt:formatDate value="${user.lastAccess}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                            <td>${user.totalTime}</td>
                            <td>${user.uniqueUrls}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty topUsers}">
                        <tr>
                            <td colspan="6" style="text-align: center;">Nenhum dado encontrado</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
        
        <div id="geo" class="tab-content">
            <h2>Geolocalização</h2>
            <div class="geo-grid">
                <div>
                    <h3>Top Países</h3>
                    <table>
                        <thead>
                            <tr>
                                <th>País</th>
                                <th>Acessos</th>
                                <th>Visitantes Únicos</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${geoStats.countries}" var="country">
                                <tr>
                                    <td>${country.location}</td>
                                    <td>${country.accessCount}</td>
                                    <td>${country.uniqueVisitors}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty geoStats.countries}">
                                <tr>
                                    <td colspan="3" style="text-align: center;">Nenhum dado encontrado</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
                <div>
                    <h3>Top Cidades</h3>
                    <table>
                        <thead>
                            <tr>
                                <th>Cidade</th>
                                <th>Acessos</th>
                                <th>Visitantes Únicos</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${geoStats.cities}" var="city">
                                <tr>
                                    <td>${city.location}</td>
                                    <td>${city.accessCount}</td>
                                    <td>${city.uniqueVisitors}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty geoStats.cities}">
                                <tr>
                                    <td colspan="3" style="text-align: center;">Nenhum dado encontrado</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        
        <div id="logs" class="tab-content">
            <h2>Logs de Acesso</h2>
            <table>
                <thead>
                    <tr>
                        <th>Data/Hora</th>
                        <th>IP</th>
                        <th>Usuário</th>
                        <th>URL</th>
                        <th>Status</th>
                        <th>Tempo</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${recentLogs}" var="log">
                        <tr>
                            <td><fmt:formatDate value="${log.timestamp}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                            <td>${log.ipAddress}</td>
                            <td>${empty log.username ? '-' : log.username}</td>
                            <td>${log.url}</td>
                            <td>
                                <span class="status ${log.statusCode < 400 ? 'status-normal' : (log.statusCode < 500 ? 'status-warning' : 'status-error')}">
                                    ${log.statusCode}
                                </span>
                            </td>
                            <td>${log.responseTime}ms</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty recentLogs}">
                        <tr>
                            <td colspan="6" style="text-align: center;">Nenhum dado encontrado</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
    
    <script>
        function openTab(evt, tabName) {
            var i, tabcontent, tablinks;
            
            // Esconde todos os conteúdos das abas
            tabcontent = document.getElementsByClassName("tab-content");
            for (i = 0; i < tabcontent.length; i++) {
                tabcontent[i].className = tabcontent[i].className.replace(" active", "");
            }
            
            // Remove a classe "active" de todas as abas
            tablinks = document.getElementsByClassName("tab");
            for (i = 0; i < tablinks.length; i++) {
                tablinks[i].className = tablinks[i].className.replace(" active", "");
            }
            
            // Mostra o conteúdo da aba atual e adiciona a classe "active" ao botão que abriu a aba
            document.getElementById(tabName).className += " active";
            evt.currentTarget.className += " active";
        }
    </script>
</body>
</html>
