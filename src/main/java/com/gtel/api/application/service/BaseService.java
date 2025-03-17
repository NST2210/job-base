package com.gtel.api.application.service;

import com.gtel.api.domains.exceptions.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BaseService {

    private final KeycloakService keycloakService;

    public Keycloak getInstance() {
        return keycloakService.getInstance();
    }

    public String getCurrentUsername() throws ApplicationException {
        return keycloakService.getCurrentUsername();
    }

    public String getCurrentUserId() throws ApplicationException {
        return keycloakService.getCurrentUserId();
    }


    public String getCurrentEmail() throws ApplicationException {
        return keycloakService.getCurrentEmail();
    }

    public String getCurrentName() throws ApplicationException {
        return keycloakService.getCurrentName();
    }
}
