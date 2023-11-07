package org.devcourse.resumeme.global.auth.service.jwt;

public record Token(String accessToken, String refreshToken) {

    public static final String ACCESS_TOKEN_NAME = "Authorization";

    public static final String REFRESH_TOKEN_NAME = "Refresh-Token";

}
