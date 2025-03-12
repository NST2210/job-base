package com.gtel.api.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "org.jobrunr.datasource")
@Data
public class JobRunrDatasourceConfig {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
