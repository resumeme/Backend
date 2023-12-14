package org.devcourse.resumeme.business.mail;

public enum EmailType {
    MENTOR_APPROVED("멘토 승인이 완료되었습니다.", "mentorApproved"),
    EVENT_CREATED("첨삭 이벤트가 등록되었습니다!", "eventCreated");

    public final String subject;
    public final String templateName;

    EmailType(String subject, String templateName) {
        this.subject = subject;
        this.templateName = templateName;
    }

}
