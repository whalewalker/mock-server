package com.mockserver.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockserver.model.response.TokenResponse;
import com.mockserver.model.shared.Response;
import com.mockserver.model.shared.UserPrincipal;
import com.mockserver.security.TokenProvider;
import com.mockserver.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        if (response.isCommitted()) {
            log.debug("Response has already been committed");
            return;
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        TokenResponse tokenResponse = authService.createTokenResponse(userPrincipal);

        Response<TokenResponse> authResponse = Response.<TokenResponse>builder()
                .statusCode("00")
                .message("Authentication successful")
                .data(tokenResponse)
                .build();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getOutputStream(), authResponse);
    }
}