# sistemademonitoramentodesite2025


### Sistema de Monitoramento de Site em Java para JBOSS 4.0.5GA

 sistema de monitoramento de site em Java compatível com JBOSS 4.0.5GA, Java 8 e SQL Server. Este sistema capturará URLs acessadas, IPs, nomes de usuários, geolocalização e logs detalhados.

## Arquitetura do Sistema

![image](https://github.com/user-attachments/assets/993c89b0-8f20-4c92-8475-cc33fa58ea4c)
![image](https://github.com/user-attachments/assets/e7550247-8684-41c4-8f68-7993ad4d6ddf)




## Estrutura do Sistema

Este sistema de monitoramento de site foi desenvolvido para JBOSS 4.0.5GA, Java 8 e SQL Server, conforme solicitado. Ele permite monitorar URLs mais acessadas, IPs, nomes de usuários, geolocalização e logs detalhados.

### Componentes Principais

1. **Filtro de Requisições (RequestLogFilter)**

1. Intercepta todas as requisições HTTP
2. Registra informações como IP, URL, tempo de resposta, etc.
3. Armazena os dados no SQL Server



2. **Serviço de Geolocalização (GeoIPLookup)**

1. Utiliza a biblioteca MaxMind GeoIP2 para obter informações de localização
2. Identifica país e cidade com base no endereço IP



3. **Camada de Acesso a Dados (AccessLogDAO)**

1. Implementa consultas SQL para obter estatísticas
2. Fornece métodos para filtrar e analisar os dados coletados



4. **Modelos de Dados**

1. AccessLog: Representa um registro de acesso
2. UrlStat: Estatísticas de URLs
3. IpStat: Estatísticas de IPs
4. UserStat: Estatísticas de usuários
5. GeoStat: Estatísticas de geolocalização



5. **Interface Web**

1. Servlet para processar requisições (MonitorServlet)
2. JSPs para renderizar a interface do usuário
3. Painel de controle com abas para diferentes tipos de estatísticas





### Funcionalidades Implementadas

- **Monitoramento de URLs**

- Ranking das páginas mais acessadas
- Tempo médio de resposta
- Taxa de rejeição



- **Análise de IPs**

- Contagem de acessos por IP
- Detecção de comportamentos suspeitos
- Geolocalização dos visitantes



- **Rastreamento de Usuários**

- Monitoramento de usuários autenticados
- Contagem de sessões
- Tempo total de navegação



- **Geolocalização**

- Distribuição de acessos por país e cidade
- Contagem de visitantes únicos por região



- **Logs Detalhados**

- Registro completo de todas as requisições
- Filtros por data, IP, URL, etc.
- Visualização de códigos de status HTTP





### Instruções de Implantação

1. **Configuração do Banco de Dados**

1. Execute o script SQL `create_tables.sql` no SQL Server
2. Configure o DataSource no JBOSS com o nome `java:/jdbc/MonitorDS`



2. **Biblioteca de Geolocalização**

1. Baixe o arquivo de banco de dados GeoLite2-City.mmdb do site da MaxMind
2. Coloque o arquivo no diretório de dados do JBOSS (`jboss.server.data.dir`)



3. **Implantação da Aplicação**

1. Compile o projeto e gere um arquivo WAR
2. Implante o WAR no servidor JBOSS 4.0.5GA
3. Acesse a aplicação através da URL `/monitor`





Este sistema fornece uma solução completa para monitoramento de site, permitindo análises detalhadas do comportamento dos visitantes e da performance do site.
