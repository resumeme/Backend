package org.devcourse.resumeme.global.auth.filter.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CLIENT_ID;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CLIENT_SECRET;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CODE;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.GRANT_TYPE;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REDIRECT_URI;

@Slf4j
public class OAuthTokenResolver {

    private final RestOperations restOperations;

    public OAuthTokenResolver() {
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), new OAuth2AccessTokenResponseHttpMessageConverter()));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        this.restOperations = restTemplate;
    }

    public OAuth2AccessTokenResponse resolve(ClientRegistration clientRegistration, String code) {
        HttpHeaders headers = getHttpHeaders();
        MultiValueMap<String, String> parameters = getBody(clientRegistration, code);
        URI uri = requestUri(clientRegistration);
        log.info("request uri : {}", uri);

        return getResponse(new RequestEntity<>(parameters, headers, HttpMethod.POST, uri)).getBody();
    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(APPLICATION_JSON));
        MediaType contentType = MediaType.valueOf(APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        headers.setContentType(contentType);

        return headers;
    }

    private static MultiValueMap<String, String> getBody(ClientRegistration clientRegistration, String code) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add(GRANT_TYPE, AUTHORIZATION_CODE.getValue());
        parameters.add(CODE, code);
        parameters.add(REDIRECT_URI, clientRegistration.getRedirectUri());
        parameters.add(CLIENT_ID, clientRegistration.getClientId());
        parameters.add(CLIENT_SECRET, clientRegistration.getClientSecret());

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
            log.info("request to oauth to get Token");

            return restOperations.exchange(request, OAuth2AccessTokenResponse.class);
        } catch (RestClientException ex) {
            throw new IllegalArgumentException();
        }
    }

}
