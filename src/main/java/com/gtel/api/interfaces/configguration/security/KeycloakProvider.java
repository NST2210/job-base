package com.gtel.api.interfaces.configguration.security;

import com.gtel.api.interfaces.configguration.SystemPropertyConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@RequiredArgsConstructor
public class KeycloakProvider {
    private final SystemPropertyConfiguration systemPropertyConfiguration;

    private static Keycloak keycloak = null;

    public Keycloak getInstance() {
        if(keycloak == null) {
            return KeycloakBuilder.builder()
                    .realm(systemPropertyConfiguration.getKeycloak().realm())
                    .serverUrl(systemPropertyConfiguration.getKeycloak().authServerUrl())
                    .clientId(systemPropertyConfiguration.getKeycloak().resource())
                    .clientSecret((String) systemPropertyConfiguration.getKeycloak().credentials().get("secret"))
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .build();
        }
        return keycloak;
    }
}
