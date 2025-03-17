package com.gtel.api.application.service;

import com.gtel.api.interfaces.models.LoginRequest;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    AccessTokenResponse login(LoginRequest request);

    String getUserId();
}
