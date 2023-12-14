package org.devcourse.resumeme.global.exception;

import org.devcourse.resumeme.common.domain.DocsEnumType;

public enum ExceptionCode implements DocsEnumType {

    MENTEE_NOT_FOUND("해당 멘티를 찾을 수 없습니다"),
    MENTOR_NOT_FOUND("해당 멘토를 찾을 수 없습니다"),
    INFO_NOT_FOUND("회원 임시 정보를 찾을 수 없습니다"),
    CAREERYEAR_NOT_ALLOWED("경력 연차가 올바르지 않습니다"),
    RESUME_NOT_FOUND("해당 이력서를 찾을 수 없습니다"),
    EVENT_NOT_FOUND("이벤트를 찾을 수 없습니다"),
    PROJECT_NOT_FOUND("해당 프로젝트를 찾을 수 없습니다"),
    COMPONENT_NOT_FOUND("등록되지 않은 이력서 블록입니다"),
    APPLICANT_NOT_FOUND("신청 이력이 없습니다"),
    APPLICATION_NOT_FOUND("예약 이력을 찾을 수 없습니다"),
    GPA_ERROR("최대 학점은 내 학점보다 커야 합니다."),
    NO_EMPTY_VALUE("빈 값일 수 없습니다"),
    TEXT_LENGTH_ERROR("텍스트 길이를 다시 확인해주세요"),
    TEXT_OVER_LENGTH("텍스트 길이가 기준을 초과합니다"),
    MENTEE_ONLY_RESUME("멘티만 이력서를 작성할 수 있습니다"),
    ROLE_NOT_ALLOWED("허용되지 않은 역할입니다"),
    MENTOR_ALREADY_APPROVED("이미 승인된 멘토입니다"),
    DUPLICATE_APPLICATION_EVENT("이미 신청한 이력이 있습니다"),
    NOT_OPEN_TIME("예약한 오픈 시간이 아닙니다"),
    RANGE_MAXIMUM_ATTENDEE("참여 인원 수를 2~10명 사이에서 정해주세요"),
    NO_REMAIN_SEATS("이미 모든 신청이 마감되었습니다"),
    NO_AVAILABLE_SEATS("잔여 자리가 없어서 재 오픈이 불가능합니다"),
    EVENT_REJECTED("거절된 이벤트입니다"),
    CANNOT_OPEN_EVENT("예약한 이벤트에 한에서만 오픈 신청을 할 수 있습니다"),
    TIME_ERROR("시간 순서를 다시 확인해주세요"),
    CAN_NOT_RESERVATION("현재 시간보다 이전 시간으로는 예약할 수 없습니다"),
    DUPLICATED_EVENT_OPEN("이미 오픈된 이벤트가 있습니다"),
    SERVER_ERROR("서버 에러입니다"),
    INVALID_ACCESS_TOKEN("유효하지 않은 액세스 토큰입니다"),
    BAD_REQUEST("잘못된 요청입니다"),
    LOGIN_REQUIRED("로그인이 필요합니다"),
    INVALID_EMAIL("이메일이 유효하지 않습니다"),
    NOT_FOUND_DATA("정보를 찾을 수 없습니다"),
    COMMENT_NOT_FOUND("작성된 피드백이 없습니다"),
    NOT_FOUND_SNAPSHOT("저장된 스냅샷이 없습니다"),
    EXCEEDED_FOLLOW_MAX("팔로우 가능한 멘토 인원이 초과되었습니다"),
    ALREADY_FOLLOWING("이미 팔로우 중인 멘토입니다"),
    NOT_FOLLOWING_NOW("현재 팔로우 하고있지 않은 멘토입니다"),
    NOT_AVAILABLE_COMMENT_TIME("첨삭 가능한 시간이 아닙니다"),
    NOT_UPDATE_EVENT("이벤트를 수정 할 수 없습니다"),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다");

    private final String message;

    ExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String getType() {
        return name();
    }

    @Override
    public String getDescription() {
        return message;
    }

}
