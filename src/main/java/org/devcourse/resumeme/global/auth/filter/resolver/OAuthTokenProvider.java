package org.devcourse.resumeme.global.auth.filter.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Slf4j
public class OAuthTokenProvider implements AuthenticationProvider {

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> userService;

    private final ClientRegistrationRepository clientRegistrationRepository;

    private final OAuthTokenResolver resolver;

    public OAuthTokenProvider(OAuth2UserService<OAuth2UserRequest, OAuth2User> userService, ClientRegistrationRepository clientRegistrationRepository,
            OAuthTokenResolver oAuthTokenResolver) {
        this.userService = userService;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.resolver = oAuthTokenResolver;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuthAuthenticationToken token = (OAuthAuthenticationToken) authentication;
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(token.getLoginProvider());

        OAuth2AccessToken accessToken = resolver.resolve(clientRegistration, token.getCode()).getAccessToken();
        log.info("AccessToken get from OAuth Server : {}", accessToken.getTokenValue());

        OAuth2User oAuth2User = userService.loadUser(new OAuth2UserRequest(clientRegistration, accessToken));

        return new OAuth2AuthenticationToken(oAuth2User, oAuth2User.getAuthorities(), clientRegistration.getRegistrationId());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuthAuthenticationToken.class.equals(authentication);
    }

}
