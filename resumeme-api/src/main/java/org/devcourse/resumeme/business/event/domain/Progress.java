package org.devcourse.resumeme.business.event.domain;

import org.devcourse.resumeme.common.domain.DocsEnumType;

public enum Progress implements DocsEnumType {
    APPLY("참여 중"),
    FEEDBACK_COMPLETE("첨삭 완료"),
    REJECT("반려 됨"),
    COMPLETE("완료")
    ;

    private final String description;

    Progress(String description) {
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

    public boolean attending() {
        return this.equals(APPLY);
    }

    public boolean isReject() {
        return this.equals(REJECT);
    }
}
