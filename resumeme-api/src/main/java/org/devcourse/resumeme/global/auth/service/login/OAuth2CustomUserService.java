package org.devcourse.resumeme.global.auth.service.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.business.user.repository.UserRepository;
import org.devcourse.resumeme.global.auth.model.UserCommonInfo;
import org.devcourse.resumeme.global.auth.model.login.OAuth2CustomUser;
import org.devcourse.resumeme.global.auth.model.login.info.OAuth2UserInfo;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2CustomUserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final OAuth2InfoRedisService oAuth2InfoRedisService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String providerName = userRequest.getClientRegistration().getRegistrationId();

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo userInfo = Provider.of(providerName).convert(oAuth2User.getAttributes());

        String email = userInfo.getEmail();

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            String cacheKey = oAuth2InfoRedisService.create(userInfo.toOAuth2TempInfo());
            log.info("Redis Temporarily saved key : {}", cacheKey);

            return new OAuth2CustomUser(null, Map.of("key", cacheKey));
        }

        UserCommonInfo userCommonInfo = UserCommonInfo.of(user.get());

        return new OAuth2CustomUser(userCommonInfo, oAuth2User.getAttributes());
    }

}
