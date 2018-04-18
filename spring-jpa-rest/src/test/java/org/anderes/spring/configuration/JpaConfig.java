package org.anderes.spring.configuration;

import java.util.Properties;

import org.anderes.edu.dbunitburner.CustomDataTypeFactory;
import org.anderes.edu.dbunitburner.DbUnitRule;
import org.dbunit.DataSourceDatabaseTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("org.anderes.cookbook.domain")
@ComponentScan(basePackages = { "org.anderes.tech" } )
@Import({ DataSourceConfig.class })
public class JpaConfig {

    @Autowired
    private DataSourceConfig dataSourceService; 
    
    @Bean(name="entityManagerFactory")
    public AbstractEntityManagerFactoryBean getEntityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSourceService.getDerbyEmbeddedDataSource());
        entityManagerFactoryBean.setPackagesToScan("org.anderes.cookbook.domain");
        entityManagerFactoryBean.setJpaDialect(new EclipseLinkJpaDialect());
        entityManagerFactoryBean.setJpaVendorAdapter(new EclipseLinkJpaVendorAdapter());
        entityManagerFactoryBean.setJpaProperties(getJpaProperties());
        return entityManagerFactoryBean;
    }
    
    private Properties getJpaProperties() {
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
    
    @Bean(name="transactionManager")
    public JpaTransactionManager getJpaTransactionManager() {
        return new JpaTransactionManager();
    }
    
    /*
    @Bean
    public DataSource getDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
        dataSource.setUrl("jdbc:derby:memory:myDB;create=true");
        dataSource.setUsername("APP");
        dataSource.setPassword("APP");
        return dataSource;
    }
    */
    
    @Bean
    public DataSourceDatabaseTester getDatabaseTester() {
        return new DataSourceDatabaseTester(dataSourceService.getDerbyEmbeddedDataSource());
    }
    
    @Bean
    public CustomDataTypeFactory getCustomDataTypeFactory() {
        return new CustomDataTypeFactory();
    }
    
    @Bean
    public DbUnitRule getDbUnitRule() {
        return new DbUnitRule(getDatabaseTester());
    }
}
