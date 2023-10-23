package org.devcourse.resumeme.domain.user;

public enum Provider {
    KAKAO("kakao"),
    GOOGLE("google");

    private final String providerName;

    Provider(String providerName) {
        this.providerName = providerName;
    }
}
