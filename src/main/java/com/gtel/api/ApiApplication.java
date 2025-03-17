package com.gtel.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients
@Log4j2
@ConfigurationPropertiesScan
@EnableJpaRepositories(
		basePackages = "com.gtel.api.infrastracture.repository.postgres",
		entityManagerFactoryRef = "writeEntityManagerFactory",
		transactionManagerRef = "writeTransactionManager"
)
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
