package com.monitor.util;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import org.apache.log4j.Logger;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;

/**
 * Utilitário para obter informações de geolocalização a partir de endereços IP
 * usando a base de dados MaxMind GeoIP2.
 */
public class GeoIPLookup {
    
    private static final Logger logger = Logger.getLogger(GeoIPLookup.class);
    private DatabaseReader reader;
    
    /**
     * Inicializa o leitor de banco de dados GeoIP
     */
    public void init() throws IOException {
        // Caminho para o arquivo de banco de dados GeoIP2
        // Nota: Este arquivo deve ser baixado do site da MaxMind e colocado no diretório apropriado
        File database = new File(System.getProperty("jboss.server.data.dir") + "/GeoLite2-City.mmdb");
        
        if (!database.exists()) {
            logger.warn("Arquivo de banco de dados GeoIP não encontrado: " + database.getAbsolutePath());
            logger.warn("A funcionalidade de geolocalização não estará disponível");
            return;
        }
        
        // Cria um leitor de banco de dados
        reader = new DatabaseReader.Builder(database).build();
        logger.info("GeoIPLookup inicializado com sucesso");
    }
    
    /**
     * Obtém o país associado ao endereço IP
     */
    public String getCountry(String ipAddress) throws IOException, GeoIp2Exception {
        if (reader == null) {
            return "Desconhecido";
        }
        
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            
            // Ignora IPs locais
            if (address.isLoopbackAddress() || address.isSiteLocalAddress()) {
                return "Local";
            }
            
            CountryResponse response = reader.country(address);
            return response.getCountry().getName();
        } catch (Exception e) {
            logger.warn("Erro ao obter país para IP: " + ipAddress, e);
            return "Desconhecido";
        }
    }
    
    /**
     * Obtém a cidade associada ao endereço IP
     */
    public String getCity(String ipAddress) throws IOException, GeoIp2Exception {
        if (reader == null) {
            return "Desconhecida";
        }
        
        try {
            InetAddress address = InetAddress.getByName(ipAddress);
            
            // Ignora IPs locais
            if (address.isLoopbackAddress() || address.isSiteLocalAddress()) {
                return "Local";
            }
            
            CityResponse response = reader.city(address);
            return response.getCity().getName();
        } catch (Exception e) {
            logger.warn("Erro ao obter cidade para IP: " + ipAddress, e);
            return "Desconhecida";
        }
    }
    
    /**
     * Fecha o leitor de banco de dados
     */
    public void close() {
        if (reader != null) {
            try {
                reader.close();
                logger.info("GeoIPLookup fechado com sucesso");
            } catch (IOException e) {
                logger.error("Erro ao fechar leitor de banco de dados GeoIP", e);
            }
        }
    }
}
