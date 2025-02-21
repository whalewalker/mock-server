package com.mockserver.controller.api.v1;

import com.mockserver.model.request.RefreshTokenRequest;
import com.mockserver.model.response.TokenResponse;
import com.mockserver.model.response.UserResponse;
import com.mockserver.model.shared.Response;
import com.mockserver.model.shared.UserPrincipal;
import com.mockserver.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;

import static com.mockserver.util.Utils.successfulResponse;

@RestController
@RequestMapping("/api/v1/oauth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/token/refresh")
    public Callable<ResponseEntity<Response<TokenResponse>>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return () -> ResponseEntity.ok(successfulResponse(authService.refreshToken(request.getRefreshToken())));
    }

    @PostMapping("/token/revoke")
    @PreAuthorize("isAuthenticated()")
    public Callable<ResponseEntity<Response<String>>> revokeToken(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return () -> {
            authService.revokeTokens(userPrincipal.getId());
            return ResponseEntity.ok(successfulResponse(null));
        };
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public Callable<ResponseEntity<Response<UserResponse>>> getUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return () -> ResponseEntity.ok(successfulResponse(UserResponse.from(userPrincipal)));
    }
}