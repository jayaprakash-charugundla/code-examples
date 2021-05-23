package com.jc.spring.batch.config.db;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

import org.graalvm.compiler.nodes.cfg.HIRLoop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties("app.mysql.datasource")
    public DatasourceProperties mysqlDatasourceProperties() {
        return new DatasourceProperties();
    }

    @Bean
    @ConfigurationProperties("app.postgres.datasource")
    public DatasourceProperties postgresDatasourceProperties() {
        return new DatasourceProperties();
    }

    @Bean(name = "mysqlDataSource")
    public DataSource mysqlDataSource(@Qualifier("mysqlDatasourceProperties")
                                                  DatasourceProperties datasourceProperties) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(datasourceProperties.getDriverClassName());
        hikariDataSource.setJdbcUrl("jdbc:mysql://" + datasourceProperties.getUrl());
        hikariDataSource.setUsername(datasourceProperties.getUsername());
        hikariDataSource.setPassword(datasourceProperties.getPassword());
        return hikariDataSource;
    }

    @Bean(name = "postgresDataSource")
    @Primary
    public DataSource postgresDataSource(@Qualifier("postgresDatasourceProperties")
                                                     DatasourceProperties datasourceProperties) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(datasourceProperties.getDriverClassName());
        hikariDataSource.setJdbcUrl("jdbc:postgresql://" + datasourceProperties.getUrl());
        hikariDataSource.setUsername(datasourceProperties.getUsername());
        hikariDataSource.setPassword(datasourceProperties.getPassword());
        return hikariDataSource;
    }

    @Bean(name = "postgresJdbcTemplate")
    @Autowired
    public JdbcTemplate postgresJdbcTemplate(@Qualifier("postgresDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    NamedParameterJdbcTemplate jdbcTemplate(@Qualifier("mysqlDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

}

