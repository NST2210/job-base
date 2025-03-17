package com.gtel.api.application.service.ipml;

import com.gtel.api.application.integration.KeycloakRestApi;
import com.gtel.api.application.service.BaseService;
import com.gtel.api.application.service.KeycloakService;
import com.gtel.api.application.service.UserService;
import com.gtel.api.domains.exceptions.ApplicationException;
import com.gtel.api.interfaces.configguration.SystemPropertyConfiguration;
import com.gtel.api.interfaces.models.LoginRequest;
import com.gtel.api.utils.ERROR_CODE;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RefreshScope
@Log4j2
public class UserServiceImpl extends BaseService implements UserService {

    private final SystemPropertyConfiguration systemPropertyConfiguration;
    private final KeycloakRestApi keycloakRestApi;

    public UserServiceImpl(SystemPropertyConfiguration systemPropertyConfiguration, KeycloakRestApi keycloakRestApi, KeycloakService keycloakService) {
        super(keycloakService);
        this.systemPropertyConfiguration = systemPropertyConfiguration;
        this.keycloakRestApi = keycloakRestApi;
    }

    @Override
    public AccessTokenResponse login(LoginRequest request) throws ApplicationException {
        Map<String, String> data = new HashMap<>();
        data.put("client_id", systemPropertyConfiguration.getKeycloak().resource());
        data.put("grant_type", systemPropertyConfiguration.getKeycloak().grantType());
        data.put("client_secret", (String) systemPropertyConfiguration.getKeycloak().credentials().get("secret"));
        data.put("username", request.getUsername());
        data.put("password", request.getPassword());
        try {
            return keycloakRestApi.login(data);
        } catch (Exception e) {
            log.error("Error when login", e);
            throw new ApplicationException(ERROR_CODE.UNAUTHORIZED);
        }
    }

    @Override
    public String getUserId() throws ApplicationException{
        String username = getCurrentUsername();
        Keycloak keycloak = getInstance();
        UsersResource usersResource = keycloak.realm(systemPropertyConfiguration.getKeycloak().realm()).users();
        List<UserRepresentation> users = usersResource.search(username);
        if (users.isEmpty()) {
            throw new ApplicationException(ERROR_CODE.INTERNAL_SERVER_ERROR, "User not found");
        }
        UserRepresentation user = users.getFirst();
        String id = user.getId();
        String userId = getCurrentUserId();
        boolean check = Objects.equals(userId, id);
        return "User co: userId: " + id + ", id: " + id + ", check: " + check;
    }
}
