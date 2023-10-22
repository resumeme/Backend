package org.devcourse.resumeme.domain.user;

public enum Role {
    ROLE_GUEST, // 소셜로그인 만 마친 상태 (추가 필수 정보 기입 x)
    ROLE_MENTEE,// 소셜로그인 후 추가 필수 정보 기입 (멘티)
    ROLE_TEMP, // 소셜로그인 후 추가 필수 정보 기입 (멘토) + 아직 멘토 승인 요청 안보낸 상태
    ROLE_PENDING, // 소셜로그인 후 추가 필수 정보 기입 (멘토) + 멘토 승인 요청 보내고 심사 대기중
    ROLE_MENTOR, // 멘토 승인 완료
    ROLE_ADMIN;

    public void checkSignUpRequest() {
        if (this != Role.ROLE_TEMP && this != Role.ROLE_MENTEE) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
    }
}
