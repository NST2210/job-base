package com.gtel.api.application.service;

import com.google.gson.JsonObject;
import com.gtel.api.domains.exceptions.ApplicationException;
import com.gtel.api.interfaces.configguration.security.KeycloakProvider;
import com.gtel.api.utils.CONSTANT;
import com.gtel.api.utils.ERROR_CODE;
import com.gtel.api.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@RequiredArgsConstructor
public class KeycloakService {

    private final HttpServletRequest httpServletRequest;

    private final KeycloakProvider keycloakProvider;

    public Keycloak getInstance() {
        return keycloakProvider.getInstance();
    }

    public String getCurrentUsername() throws ApplicationException {
        String token = getCurrentToken();
        try {
            JsonObject jsonObject = JwtUtils.toJson(token);
            return jsonObject.get("preferred_username").getAsString();
        } catch (Exception e) {
            throw new ApplicationException(ERROR_CODE.INTERNAL_SERVER_ERROR, "Invalid token");
        }
    }

    public String getCurrentUserId() throws ApplicationException {
        String token = getCurrentToken();
        try {
            JsonObject jsonObject = JwtUtils.toJson(token);
            return jsonObject.get("sub").getAsString();
        } catch (Exception e) {
            throw new ApplicationException(ERROR_CODE.INTERNAL_SERVER_ERROR, "Invalid token");
        }
    }

    public String getCurrentEmail() throws ApplicationException {
        String token = getCurrentToken();
        try {
            JsonObject jsonObject = JwtUtils.toJson(token);
            return jsonObject.get("email").getAsString();
        } catch (Exception e) {
            throw new ApplicationException(ERROR_CODE.INTERNAL_SERVER_ERROR, "Invalid token");
        }
    }

    public String getCurrentName() throws ApplicationException {
        String token = getCurrentToken();
        try {
            JsonObject jsonObject = JwtUtils.toJson(token);
            return jsonObject.get("given_name").getAsString();
        } catch (Exception e) {
            throw new ApplicationException(ERROR_CODE.INTERNAL_SERVER_ERROR, "Invalid token");
        }
    }

    public String getCurrentToken() throws ApplicationException {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String token = "";
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(CONSTANT.AUTHORIZATION.BEARER)) {
            token = authorizationHeader.substring(7);
        }
        if (token.isEmpty()) {
            throw new ApplicationException(ERROR_CODE.BAD_REQUEST);
        }
        return token;
    }
}
