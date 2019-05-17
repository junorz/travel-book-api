package com.junorz.travelbook.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.junorz.travelbook.config.DbConfig.DbInfo;
import com.junorz.travelbook.context.orm.DefaultRepository;
import com.junorz.travelbook.context.orm.Repository;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Data;

/**
 * Configuration of Database
 */
@Configuration
@EnableConfigurationProperties(DbInfo.class)
public class DbConfig {

    @ConfigurationProperties("travelbook.datasource")
    @Data
    class DbInfo {
        private String jdbcUrl;
        private String username;
        private String password;
        private String ddl;
        private boolean showSql;
        private boolean createSchemeDdlScript;

        public JpaVendorAdapter getJpaVendorAdapter() {
            HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
            jpaVendorAdapter.setShowSql(showSql);
            return jpaVendorAdapter;
        }

        public Map<String, String> getJpaProperties() {
            Map<String, String> jpaProperties = new HashMap<>();
            jpaProperties.put("hibernate.hbm2ddl.auto", ddl);
            if (createSchemeDdlScript) {
                jpaProperties.put("javax.persistence.schema-generation.create-source", "metadata");
                jpaProperties.put("javax.persistence.schema-generation.scripts.action", "create");
                jpaProperties.put("javax.persistence.schema-generation.scripts.create-target", "ddl.sql");
            }
            return jpaProperties;
        }

    }

    @Bean
    public DataSource dataSource(DbInfo dbInfo) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dbInfo.getJdbcUrl());
        dataSource.setUsername(dbInfo.getUsername());
        dataSource.setPassword(dbInfo.getPassword());
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DbInfo dbInfo, DataSource dataSource) {
        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(dbInfo.getJpaVendorAdapter(),
                dbInfo.getJpaProperties(), null);
        return builder
                .dataSource(dataSource)
                .jta(false)
                .persistenceUnit("travelbook")
                .packages("com.junorz.travelbook.domain")
                .build();
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean) {
        return new JpaTransactionManager(localContainerEntityManagerFactoryBean.getObject());
    }
    
    @Bean
    public Repository repository() {
        return new DefaultRepository();
    }
    

}
