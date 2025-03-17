package com.gtel.api.interfaces.configguration.security;

import com.gtel.api.domains.exceptions.ApplicationException;
import com.gtel.api.interfaces.configguration.SystemPropertyConfiguration;
import com.gtel.api.utils.ERROR_CODE;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class KeycloakResolverConfiguration {
    private final SystemPropertyConfiguration systemPropertyConfiguration;

    @Bean
    public KeycloakConfigResolver keycloakConfigResolver() {
        return new KeycloakConfigResolver() {

            private KeycloakDeployment keycloakDeployment;

            @Override
            public KeycloakDeployment resolve(HttpFacade.Request request) {
                if (Objects.nonNull(keycloakDeployment)) {
                    return keycloakDeployment;
                }
                if (Objects.isNull(systemPropertyConfiguration) || Objects.isNull(systemPropertyConfiguration.getKeycloak())) {
                    throw new ApplicationException(ERROR_CODE.INTERNAL_SERVER_ERROR);
                } else {
                    AdapterConfig adapterConfig = getAdapterConfig();
                    keycloakDeployment = KeycloakDeploymentBuilder.build(adapterConfig);
                }
                return keycloakDeployment;
            }
        };
    }

    private AdapterConfig getAdapterConfig() {
        AdapterConfig adapterConfig = new AdapterConfig();
        adapterConfig.setRealm(systemPropertyConfiguration.getKeycloak().realm());
        adapterConfig.setSslRequired(systemPropertyConfiguration.getKeycloak().sslRequired());
        adapterConfig.setResource(systemPropertyConfiguration.getKeycloak().resource());
        adapterConfig.setCredentials(systemPropertyConfiguration.getKeycloak().credentials());
        PolicyEnforcerConfig policyEnforcerConfig = new PolicyEnforcerConfig();
        policyEnforcerConfig.setHttpMethodAsScope(systemPropertyConfiguration.getKeycloak().policyEnforcer().httpMethodAsScope());
        return adapterConfig;
    }

}
