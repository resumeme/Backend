package org.devcourse.resumeme.domain.user;

import org.devcourse.resumeme.global.advice.exception.CustomException;

public enum Role {
    ROLE_GUEST, // 소셜로그인 만 마친 상태 (추가 필수 정보 기입 x)
    ROLE_MENTEE,// 소셜로그인 후 추가 필수 정보 기입 (멘티)
    ROLE_PENDING, // 소셜로그인 후 추가 필수 정보 기입 (멘토)
    ROLE_MENTOR, // 멘토 승인 완료
    ROLE_ADMIN;

    public void checkSignUpRequest() {
        if (this != Role.ROLE_PENDING && this != Role.ROLE_MENTEE) {
            throw new CustomException("ROLE_CHANGE_REFUSED","해당 역할로 변경할 수 없습니다.");
        }
    }
}
