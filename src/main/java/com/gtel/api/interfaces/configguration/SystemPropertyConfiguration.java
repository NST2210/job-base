package com.gtel.api.interfaces.configguration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Getter
@ConfigurationProperties("system.configurations")
public class SystemPropertyConfiguration {

    private final String[] uriWhiteList;
    private final CorsProperty cors;
    private final KeyCloakProperty keycloak;

    public SystemPropertyConfiguration(String[] uriWhiteList, CorsProperty cors, KeyCloakProperty keycloak) {
        this.uriWhiteList = uriWhiteList;
        this.cors = cors;
        this.keycloak = keycloak;
    }

    public record CorsProperty(String allowedOriginPattern, String allowedHeader, List<String> allowedMethods,
                               boolean allowCredentials, String exposedHeader,
                               String urlBasedPatternCorsConfigurationSource) {

    }

    public record KeyCloakProperty(String realm, String authServerUrl, String sslRequired, String resource,
                                   String grantType, Map<String, Object> credentials,
                                   PolicyEnforcerProperty policyEnforcer) {

    }

    public record PolicyEnforcerProperty(boolean httpMethodAsScope) {

    }

}
