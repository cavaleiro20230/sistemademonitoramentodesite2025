-- Script SQL para criar as tabelas necessárias no SQL Server

-- Tabela para armazenar logs de acesso
CREATE TABLE access_log (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    timestamp DATETIME NOT NULL,
    ip_address VARCHAR(45) NOT NULL,
    url VARCHAR(2000) NOT NULL,
    status_code INT NOT NULL,
    user_agent VARCHAR(500),
    referer VARCHAR(2000),
    response_time BIGINT,
    country VARCHAR(100),
    city VARCHAR(100),
    username VARCHAR(100)
);

-- Índices para melhorar a performance das consultas
CREATE INDEX idx_access_log_timestamp ON access_log (timestamp);
CREATE INDEX idx_access_log_ip ON access_log (ip_address);
CREATE INDEX idx_access_log_url ON access_log (url);
CREATE INDEX idx_access_log_username ON access_log (username);
CREATE INDEX idx_access_log_country ON access_log (country);
CREATE INDEX idx_access_log_city ON access_log (city);
