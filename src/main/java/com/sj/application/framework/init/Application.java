package com.sj.application.framework.init;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot main class
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan({"com.sj.application.framework"})
@PropertySource("classpath:application.properties")
public class Application {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(Application.class);
	}
	
	


}