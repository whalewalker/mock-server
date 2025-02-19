package com.mockserver.security.oauth2;
import com.mockserver.exception.RequestIsBadException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> new GoogleOAuth2UserInfo(attributes);
            case "github" -> new GithubOAuth2UserInfo(attributes);
            default -> throw new RequestIsBadException("Unsupported OAuth2 provider: " + registrationId);
        };
    }
}