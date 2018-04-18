package org.anderes.spring.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource getDerbyEmbeddedDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
        dataSource.setUrl("jdbc:derby:memory:myDB;create=true");
        dataSource.setUsername("APP");
        dataSource.setPassword("APP");
        return dataSource;
    }
}
