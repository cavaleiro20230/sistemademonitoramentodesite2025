<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sistema de Monitoramento de Site</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            margin-top: 0;
        }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            font-weight: bold;
        }
        .btn:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Sistema de Monitoramento de Site</h1>
        <p>Bem-vindo ao Sistema de Monitoramento de Site. Este sistema permite monitorar o tráfego do seu site, incluindo:</p>
        <ul>
            <li>URLs mais acessadas</li>
            <li>IPs de acesso</li>
            <li>Nomes de usuários</li>
            <li>Geolocalização</li>
            <li>Logs detalhados</li>
        </ul>
        <p>Clique no botão abaixo para acessar o painel de monitoramento:</p>
        <a href="monitor" class="btn">Acessar Painel</a>
    </div>
</body>
</html>
