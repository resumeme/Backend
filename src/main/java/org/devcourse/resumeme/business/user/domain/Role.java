package org.devcourse.resumeme.business.user.domain;

import org.devcourse.resumeme.common.domain.DocsEnumType;
import org.devcourse.resumeme.global.exception.CustomException;

import java.util.Arrays;

public enum Role implements DocsEnumType {
    ROLE_MENTEE("mentee"),// 소셜로그인 후 추가 필수 정보 기입 (멘티)
    ROLE_PENDING("pending"), // 소셜로그인 후 추가 필수 정보 기입 (멘토)
    ROLE_MENTOR("mentor"), // 멘토 승인 완료
    ROLE_ADMIN("admin");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }


    public static Role of(String roleName) {
        return Arrays.stream(values())
                .filter(role -> role.getRoleName().equals(roleName))
                .findAny()
                .orElseThrow(() -> new CustomException("INVALID_ROLE", "존재하지 않는 권한입니다."));
    }

    @Override
    public String getType() {
        return name();
    }

    @Override
    public String getDescription() {
        return roleName;
    }

    public String getRoleName() {return roleName;}

}
