package org.devcourse.resumeme.domain.event;

import org.devcourse.resumeme.common.domain.DocsEnumType;

public enum EventStatus implements DocsEnumType {
    OPEN("첨삭 이벤트 오픈"),
    REOPEN("재 신청 시작"),
    CLOSE("이벤트 신청 마감")
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
}
