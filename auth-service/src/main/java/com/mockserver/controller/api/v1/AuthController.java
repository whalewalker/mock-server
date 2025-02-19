package com.mockserver.controller.api.v1;

import com.mockserver.model.response.TokenResponse;
import com.mockserver.model.shared.UserPrincipal;
import com.mockserver.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));
    }
    
    @PostMapping("/token/revoke")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> revokeToken(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        authService.revokeTokens(userPrincipal.getId());
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(UserResponse.from(userPrincipal));
    }
}