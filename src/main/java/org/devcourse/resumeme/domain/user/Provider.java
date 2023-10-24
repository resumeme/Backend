package org.devcourse.resumeme.domain.user;

import org.devcourse.resumeme.common.domain.DocsEnumType;

public enum Provider implements DocsEnumType {
    KAKAO("kakao"),
    GOOGLE("google");

    private final String providerName;

    Provider(String providerName) {
        this.providerName = providerName;
    }

    @Override
    public String getType() {
        return name();
    }

    @Override
    public String getDescription() {
        return providerName;
    }

}
