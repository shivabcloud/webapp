package com.project.healthcheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "com.project.healthcheck.Pojo")
@ComponentScan(basePackages = {"com.project.healthcheck.Dao","com.project.healthcheck.SpringSecurity","com.project.healthcheck.Controller","com.project.healthcheck.Service", "com.project.healthcheck.Encryption"})
public class HealthcheckApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthcheckApplication.class, args);
	}

}
