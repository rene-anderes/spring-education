package org.anderes.spring.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.anderes.edu.dbunitburner.CustomDataTypeFactory;
import org.anderes.edu.dbunitburner.DbUnitRule;
import org.dbunit.DataSourceDatabaseTester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Import({ DatabaseConfig.class })
public class JpaConfig {

    @Autowired
    private DataSource dataSource; 
    
    @Bean(name="entityManagerFactory")
    public AbstractEntityManagerFactoryBean getEntityManagerFactory(@Qualifier("database-properties") Properties jpaProperties) {
        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("org.anderes.cookbook.domain");
        entityManagerFactoryBean.setJpaDialect(new EclipseLinkJpaDialect());
        entityManagerFactoryBean.setJpaVendorAdapter(new EclipseLinkJpaVendorAdapter());
        entityManagerFactoryBean.setJpaProperties(jpaProperties);
        return entityManagerFactoryBean;
    }

    
    @Bean(name="transactionManager")
    public JpaTransactionManager getJpaTransactionManager() {
        return new JpaTransactionManager();
    }
        
    @Bean
    public DataSourceDatabaseTester getDatabaseTester() {
        return new DataSourceDatabaseTester(dataSource);
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
