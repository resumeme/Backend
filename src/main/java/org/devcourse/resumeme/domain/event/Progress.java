package org.devcourse.resumeme.domain.event;

import org.devcourse.resumeme.common.domain.DocsEnumType;

public enum Progress implements DocsEnumType {
    APPLY("참여 중"),
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
}