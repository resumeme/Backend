package org.devcourse.resumeme.business.event.domain;

import org.devcourse.resumeme.common.domain.DocsEnumType;

public enum EventStatus implements DocsEnumType {
    READY("첨삭 이벤트 오픈 예약"),
    OPEN("첨삭 이벤트 오픈"),
    REOPEN("재 신청 시작"),
    CLOSE("이벤트 신청 마감"),
    FINISH("이벤트 종료")
    ;

    private final String description;

    EventStatus(String description) {
        this.description = description;
    }

    @Override
    public String getType() {
        return name();
    }

    @Override
    public String getDescription() {
        return description;
    }

    public boolean canApply() {
        return this.equals(OPEN) || this.equals(REOPEN);
    }

    public boolean isReady() {
        return this.equals(READY);
    }

    public boolean isOpen() {
        return !(this.equals(CLOSE) || this.equals(FINISH));
    }

}
