package org.devcourse.resumeme.global.auth;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.user.RequiredInfo;
import org.devcourse.resumeme.domain.user.Provider;
import org.devcourse.resumeme.domain.user.Role;
import org.devcourse.resumeme.domain.user.User;
import org.devcourse.resumeme.global.auth.userInfo.GoogleOAuth2UserInfo;
import org.devcourse.resumeme.global.auth.userInfo.KakaoOAuth2UserInfo;
import org.devcourse.resumeme.global.auth.userInfo.OAuth2UserInfo;
import org.devcourse.resumeme.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2CustomUserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Provider socialType = Provider.valueOf(provider.toUpperCase());
        OAuth2UserInfo userInfo = null;

        if (provider.equals("google")) {
            userInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
        }

        if (provider.equals("kakao")) {
            userInfo = new KakaoOAuth2UserInfo(oAuth2User.getAttributes());
        }

        if (userInfo == null) {
            throw new RuntimeException("지원하지 않는 소셜로그인");
        }

        String oauthUsername = provider + "_" + userInfo.getId();

        User userEntity;
        OAuth2UserInfo finalUserInfo = userInfo;

        userEntity = userRepository.findByOauthUsername(oauthUsername)
                .orElseGet(() -> userRepository.save(
                        User.builder().oauthUsername(oauthUsername)
                                .imageUrl(finalUserInfo.getImageUrl())
                                .role(Role.ROLE_GUEST)
                                .password("resumeme")
                                .requiredInfo(
                                        RequiredInfo.builder()
                                                .nickname(finalUserInfo.getNickname())
                                                .build()
                                )
                                .email(finalUserInfo.getEmail())
                                .provider(socialType)
                                .build()));

        return new OAuth2CustomUser(userEntity, oAuth2User.getAttributes());
    }

}
