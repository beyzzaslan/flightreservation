package com.flight.flightreservation.config;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;



    @Value("${spring.datasource.password}")
    private String password;
    
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.potgresql.Driver");
        dataSource.serUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        try{
            dataSource.getConnection();
            logger.info("Veritabanı bağlantısı başarıyla oluşturuldu.");

        }
        catch(Exception e ){
            logger.error("Veritabanı bağlantısı oluşturuklurken bir hata oluştu : {}",e.getMessage());  
            return dataSource;
        }
    }
}
/*
Backend mantığıyla:
Spring uygulamasına PostgreSQL database bağlantısını tanıtmak.
Yani bu class:
database driver ayarlar
url ayarlar
username/password ayarlar
bağlantıyı test eder
DataSource bean oluşturur

*/
