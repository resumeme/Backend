package org.devcourse.resumeme.business.user.domain;

import org.devcourse.resumeme.common.domain.DocsEnumType;
import org.devcourse.resumeme.global.exception.CustomException;
import org.devcourse.resumeme.global.auth.model.login.info.GoogleOAuth2UserInfo;
import org.devcourse.resumeme.global.auth.model.login.info.KakaoOAuth2UserInfo;
import org.devcourse.resumeme.global.auth.model.login.info.OAuth2UserInfo;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum Provider implements DocsEnumType {
    KAKAO("kakao", (KakaoOAuth2UserInfo::new)),
    GOOGLE("google", (GoogleOAuth2UserInfo::new));

    private final String providerName;

    private final Function<Map<String, Object>, OAuth2UserInfo> convert;

    Provider(String providerName, Function<Map<String, Object>, OAuth2UserInfo> convert) {
        this.providerName = providerName;
        this.convert = convert;
    }

    public static Provider of(String providerName) {
        return Arrays.stream(values())
                .filter(provider -> provider.providerName.equals(providerName))
                .findAny()
                .orElseThrow(() -> new CustomException("NOT_SUPPORTED_PROVIDER", "지원되지 않는 소셜로그인 입니다."));
    }

    public OAuth2UserInfo convert(Map<String, Object> attributes) {
        return convert.apply(attributes);
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
