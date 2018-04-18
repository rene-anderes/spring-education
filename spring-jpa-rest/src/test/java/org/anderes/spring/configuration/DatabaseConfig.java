package org.anderes.spring.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource getDerbyEmbeddedDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
        dataSource.setUrl("jdbc:derby:memory:myDB;create=true");
        dataSource.setUsername("APP");
        dataSource.setPassword("APP");
        return dataSource;
    }
    
    @Bean(name="derby-properties")
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
}
