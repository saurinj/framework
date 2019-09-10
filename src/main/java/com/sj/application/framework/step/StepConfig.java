package com.sj.application.framework.step;

import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class StepConfig {
	
	@Bean
	public ExecutorService taskExecutor() {
        return Executors.newFixedThreadPool(30);
	}
	
	@Bean
    public DataSource dataSource(@Value("${mysql.jdbc.driver.classname}") String driverClassName,
            @Value("${mysql.jdbc.connection.url}") String connectionUrl,
            @Value("${mysql.jdbc.username}") String username,
            @Value("${mysql.jdbc.password}") String password) throws Exception {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(connectionUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(new String(Base64.getDecoder().decode(password)));
        return dataSource;
    }
}
