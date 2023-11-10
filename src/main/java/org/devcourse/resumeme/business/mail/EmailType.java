package org.devcourse.resumeme.business.mail;

public enum EmailType {
    MENTOR_APPROVED("멘토 승인이 완료되었습니다.", "mentorApproved"),
    EVENT_OPEN("첨삭 이벤트가 오픈되었습니다!", "eventOpen");

    public final String subject;
    public final String templateName;

    EmailType(String subject, String templateName) {
        this.subject = subject;
        this.templateName = templateName;
    }

}
