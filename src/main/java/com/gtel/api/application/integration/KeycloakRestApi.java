package com.gtel.api.application.integration;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(value = "keycloak", url = "${system.configurations.keycloak.base-url}")
public interface KeycloakRestApi {
    @PostMapping(path = "/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    AccessTokenResponse login(Map<String, ?> data);
}
