package org.devcourse.resumeme.global.auth;

import lombok.RequiredArgsConstructor;
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

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // kakao, google
        Provider socialType = Provider.valueOf(registrationId.toUpperCase());
        String provider = userRequest.getClientRegistration().getRegistrationId(); // google
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

        String oAuthUsername = provider + "_" + userInfo.getId();
        User userEntity = userRepository.findByOAuthUsername(oAuthUsername);

        if (userEntity == null) { // 최초 로그인
            userEntity = User.builder()
                    .oAuthUsername(oAuthUsername)
                    .imageUrl(userInfo.getImageUrl())
                    .role(Role.ROLE_GUEST)
                    .password("resumeme")
                    .nickname(userInfo.getNickname())
                    .email(userInfo.getEmail())
                    .provider(socialType)
                    .build();

            userEntity = userRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }

}
