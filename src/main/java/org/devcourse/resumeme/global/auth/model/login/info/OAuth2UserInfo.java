package org.devcourse.resumeme.global.auth.model.login.info;

import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public OAuth2TempInfo toOAuth2TempInfo() {
        return new OAuth2TempInfo(null, getProvider(), getNickname(), getEmail(), getImageUrl());
    }

    public abstract String getId();

    public abstract String getProvider();

    public abstract String getNickname();

    public abstract String getEmail();

    public abstract String getImageUrl();

}
