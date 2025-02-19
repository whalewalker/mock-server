package com.mockserver.security.oauth2;

import com.mockserver.exception.RequestFailedException;
import com.mockserver.model.entity.User;
import com.mockserver.model.enums.AuthProvider;
import com.mockserver.model.enums.RoleType;
import com.mockserver.model.shared.UserPrincipal;
import com.mockserver.repository.relational.RoleRepo;
import com.mockserver.repository.relational.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (Exception ex) {
            throw new RequestFailedException("Failed to process OAuth2 user");
        }
    }
    
    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
            oAuth2UserRequest.getClientRegistration().getRegistrationId(),
            oAuth2User.getAttributes()
        );
        
        User user = userRepo.getByEmailOptional(oAuth2UserInfo.getEmail())
            .map(existingUser -> updateExistingUser(existingUser, oAuth2UserInfo))
            .orElseGet(() -> registerNewUser(oAuth2UserRequest, oAuth2UserInfo));
        
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

  private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
      existingUser.setName(oAuth2UserInfo.getName());
      existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
      return userRepo.create(existingUser);
  }

  private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
      return userRepo.create(User.builder()
              .name(oAuth2UserInfo.getName())
              .email(oAuth2UserInfo.getEmail())
              .imageUrl(oAuth2UserInfo.getImageUrl())
              .provider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
              .providerId(oAuth2UserInfo.getId())
              .roles(new HashSet<>(roleRepo.getByNameIn(RoleType.ROLE_USER)))
              .build());
  }
}