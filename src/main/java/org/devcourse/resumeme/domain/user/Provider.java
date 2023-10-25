package org.devcourse.resumeme.domain.user;

import org.devcourse.resumeme.common.domain.DocsEnumType;
import org.devcourse.resumeme.global.advice.exception.CustomException;

import java.util.Arrays;

public enum Provider implements DocsEnumType {
    KAKAO("kakao"),
    GOOGLE("google");

    private final String providerName;

    Provider(String providerName) {
        this.providerName = providerName;
    }

    public static Provider of(String providerName) {
        return Arrays.stream(values())
                .filter(provider -> provider.providerName.equals(providerName))
                .findAny()
                .orElseThrow(() -> new CustomException("NOT_SUPPORTED_PROVIDER", "지원되지 않는 소셜로그인 입니다."));
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
