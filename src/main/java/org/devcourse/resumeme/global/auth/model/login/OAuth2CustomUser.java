package org.devcourse.resumeme.global.auth.model.login;

import lombok.Data;
import lombok.Getter;
import org.devcourse.resumeme.global.auth.model.UserCommonInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
public class OAuth2CustomUser implements OAuth2User {

    @Getter
    private UserCommonInfo userCommonInfo;

    private Map<String, Object> attributes;

    public OAuth2CustomUser(UserCommonInfo userCommonInfo, Map<String, Object> attributes) {
        this.userCommonInfo = userCommonInfo;
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return userCommonInfo.email();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> String.valueOf(userCommonInfo.role()));
    }

    public boolean isNewUser() {
        return userCommonInfo == null;
    }

    public String getAuthenticationKey() {
        return String.valueOf(attributes.get("key"));
    }
}
