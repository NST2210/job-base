package com.gtel.api.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.spring.autoconfigure.JobRunrAutoConfiguration;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.sql.postgres.PostgresStorageProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "org.jobrunr.enabled", havingValue = "true", matchIfMissing = true)
@Import(JobRunrAutoConfiguration.class)
public class JobRunnerConfig {
    @Bean
    @ConditionalOnProperty(name = "org.jobrunr.enabled", havingValue = "true", matchIfMissing = true)
    public StorageProvider storageProvider(JobRunrDatasourceConfig properties, JobMapper jobMapper) {
        DataSource dataSource = DataSourceBuilder.create()
                .url(properties.getUrl())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .driverClassName(properties.getDriverClassName())
                .build();
        PostgresStorageProvider storageProvider = new PostgresStorageProvider(dataSource);
        storageProvider.setJobMapper(jobMapper);
        return storageProvider;
    }
}
