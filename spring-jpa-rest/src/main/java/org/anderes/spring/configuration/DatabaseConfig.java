package org.anderes.spring.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfig {

    @Bean
//    @Profile("derby")
    public DataSource getDerbyEmbeddedDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.apache.derby.jdbc.ClientDriver");
        dataSource.setUrl("jdbc:derby://localhost:1527/myCookbook;create=true");
        dataSource.setUsername("APP");
        dataSource.setPassword("APP");
        return dataSource;
    }
    
    @Bean(name="database-properties")
//    @Profile("derby")
    public Properties getJpaProperties() {
        final Properties jpaProperties = new Properties();
        jpaProperties.setProperty("javax.persistence.validation.mode", "auto");
        jpaProperties.setProperty("javax.persistence.schema-generation.database.action", "create");
        jpaProperties.setProperty("javax.persistence.schema-generation.create-database-schemas", "true");
        jpaProperties.setProperty("javax.persistence.schema-generation.create-source", "metadata");
        jpaProperties.setProperty("eclipselink.target-database", "Derby");
        jpaProperties.setProperty("eclipselink.jdbc.native-sql", "true");
        jpaProperties.setProperty("eclipselink.weaving", "false");
        jpaProperties.setProperty("eclipselink.logging.level", "FINE");
        // Note: Setting eclipselink.logging.level to FINE is not sufficient (as of EclipseLink 2.4.0 - Juno),
        // you have to set eclipselink.logging.level.sql to FINE.
        jpaProperties.setProperty("eclipselink.logging.level.sql", "FINE");
        jpaProperties.setProperty("eclipselink.logging.parameters", "true");
        jpaProperties.setProperty("eclipselink.logging.logger", "JavaLogger");
        return jpaProperties;
    }
    
    @Bean
    @Profile("mysql")
    public DataSource getMySQLDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost/recipes?useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("lernwerkst@tt");
        return dataSource;
    }
    
    @Bean(name="database-properties")
    @Profile("mysql")
    public Properties getJpaMySqlProperties() {
        final Properties jpaProperties = new Properties();
        jpaProperties.setProperty("javax.persistence.validation.mode", "auto");
        jpaProperties.setProperty("javax.persistence.schema-generation.database.action", "create");
        jpaProperties.setProperty("javax.persistence.schema-generation.create-database-schemas", "true");
        jpaProperties.setProperty("javax.persistence.schema-generation.create-source", "metadata");
        jpaProperties.setProperty("eclipselink.target-database", "MySQL");
        jpaProperties.setProperty("eclipselink.jdbc.native-sql", "true");
        jpaProperties.setProperty("eclipselink.weaving", "false");
        jpaProperties.setProperty("eclipselink.logging.level", "FINE");
        // Note: Setting eclipselink.logging.level to FINE is not sufficient (as of EclipseLink 2.4.0 - Juno),
        // you have to set eclipselink.logging.level.sql to FINE.
        jpaProperties.setProperty("eclipselink.logging.level.sql", "FINE");
        jpaProperties.setProperty("eclipselink.logging.parameters", "true");
        jpaProperties.setProperty("eclipselink.logging.logger", "JavaLogger");
        return jpaProperties;
    }
}
