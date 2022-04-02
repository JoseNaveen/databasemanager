package com.joey.databasemanager.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableJpaRepositories(basePackages="com.joey.databasemanager.repository.sql")
public class PrimaryDataSourceConfiguration {
	
	
	@Bean(name = "dataSource")      
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
		return DataSourceBuilder.create()
		.url("jdbc:mysql://localhost:3306/classicmodels")
		.password("root")
		.username("root")
		.build();
    }
}	
