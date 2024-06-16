package org.devcourse.resumeme.global.auth.filter.resolver;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Objects;

public class OAuthAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private String code;

    @Getter
    private String loginProvider;

    public OAuthAuthenticationToken(String code, String loginProvider) {
        super(null);
        this.code = code;
        this.loginProvider = loginProvider;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public OAuth2User getPrincipal() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        OAuthAuthenticationToken that = (OAuthAuthenticationToken) o;
        return Objects.equals(code, that.code) && Objects.equals(loginProvider, that.loginProvider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), code, loginProvider);
    }

}
