package org.devcourse.resumeme.global.auth.model.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
public record JwtProperties(String secretKey, TokenInfo access, TokenInfo refresh) {

    public record TokenInfo(String headerName, int expiration) {

        public TokenInfo(String headerName, int expiration) {
            this.headerName = headerName;
            this.expiration = expiration * 1000;
        }

    }

}
