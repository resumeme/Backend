package org.devcourse.resumeme.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class OAuthTokenResponseFilter extends AbstractAuthenticationProcessingFilter {

    private static final String REGISTRATION_ID_URI_VARIABLE_NAME = "registrationId";

    private static final String LOGIN_URL = "/login/oauth2/code/*";

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> userService;

    private final ClientRegistrationRepository clientRegistrationRepository;

    private final AntPathRequestMatcher authorizationRequestMatcher;

    private final RestOperations restOperations;

    private final ObjectMapper objectMapper;

    public OAuthTokenResponseFilter(AuthenticationManager authenticationManager, OAuth2UserService<OAuth2UserRequest, OAuth2User> userService, ClientRegistrationRepository clientRegistrationRepository, ObjectMapper objectMapper) {
        super(LOGIN_URL, authenticationManager);
        this.userService = userService;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.objectMapper = objectMapper;
        this.authorizationRequestMatcher = new AntPathRequestMatcher(LOGIN_URL);
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), new OAuth2AccessTokenResponseHttpMessageConverter()));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        this.restOperations = restTemplate;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        CodeRequest codeRequest = objectMapper.readValue(request.getInputStream(), CodeRequest.class);

        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(codeRequest.loginProvider);
        HttpHeaders headers = getHttpHeaders();
        MultiValueMap<String, String> parameters = getBody(clientRegistration, codeRequest.code);
        URI uri = requestUri(clientRegistration);

        RequestEntity<?> requestEntity = new RequestEntity<>(parameters, headers, HttpMethod.POST, uri);
        OAuth2AccessTokenResponse tokenResponse = getResponse(requestEntity).getBody();

        OAuth2User oAuth2User = userService.loadUser(new OAuth2UserRequest(clientRegistration, tokenResponse.getAccessToken()));

        return new OAuth2AuthenticationToken(oAuth2User, oAuth2User.getAuthorities(), clientRegistration.getRegistrationId());
    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        MediaType contentType = MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        headers.setContentType(contentType);

        return headers;
    }

    private static MultiValueMap<String, String> getBody(ClientRegistration clientRegistration, String code) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add(OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.AUTHORIZATION_CODE.getValue());
        parameters.add(OAuth2ParameterNames.CODE, code);
        parameters.add(OAuth2ParameterNames.REDIRECT_URI, clientRegistration.getRedirectUri());
        parameters.add(OAuth2ParameterNames.CLIENT_ID, clientRegistration.getClientId());
        parameters.add(OAuth2ParameterNames.CLIENT_SECRET, clientRegistration.getClientSecret());

        return parameters;
    }

    private static URI requestUri(ClientRegistration clientRegistration) {
        return UriComponentsBuilder
                .fromUriString(clientRegistration.getProviderDetails().getTokenUri())
                .build()
                .toUri();
    }

    private ResponseEntity<OAuth2AccessTokenResponse> getResponse(RequestEntity<?> request) {
        try {
            return this.restOperations.exchange(request, OAuth2AccessTokenResponse.class);
        } catch (RestClientException ex) {
            throw new IllegalArgumentException();
        }
    }

    record CodeRequest(String loginProvider, String code) {

    }

}
