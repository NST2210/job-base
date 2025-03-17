package com.gtel.api.interfaces.restful;

import com.gtel.api.application.service.UserService;
import com.gtel.api.interfaces.models.LoginRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Log4j2
public class LoginController {
    private final UserService userService;

    @PostMapping
    public AccessTokenResponse login(@NotNull @RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
