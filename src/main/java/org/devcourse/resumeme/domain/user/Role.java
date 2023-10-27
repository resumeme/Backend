package org.devcourse.resumeme.domain.user;

import org.devcourse.resumeme.common.domain.DocsEnumType;
import org.devcourse.resumeme.global.advice.exception.CustomException;

import java.util.Arrays;

public enum Role implements DocsEnumType {
    ROLE_MENTEE("mentee"),// 소셜로그인 후 추가 필수 정보 기입 (멘티)
    ROLE_PENDING("pending"), // 소셜로그인 후 추가 필수 정보 기입 (멘토)
    ROLE_MENTOR("mentor"), // 멘토 승인 완료
    ROLE_ADMIN("admin");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public static Role of(String roleName) {
        return Arrays.stream(values())
                .filter(role -> role.getDescription().equals(roleName))
                .findAny()
                .orElseThrow(() -> new CustomException("INVALID_ROLE", "존재하지 않는 권한입니다."));
    }

    @Override
    public String getType() {
        return name();
    }

    @Override
    public String getDescription() {
        return role;
    }


}
