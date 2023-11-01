package org.devcourse.resumeme.domain.resume;

import org.devcourse.resumeme.common.domain.DocsEnumType;

public enum BlockType implements DocsEnumType {

    ACTIVITY("활동"),
    CAREER("업무경험"),
    CERTIFICATION("수상 및 자격증"),
    DUTY("작업 내용"),
    FOREIGN_LANGUAGE("외국어"),
    PROJECT("프로젝트"),
    SKILL("기술"),
    TRAINING("교육");

    private final String description;

    BlockType(String description) {
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
