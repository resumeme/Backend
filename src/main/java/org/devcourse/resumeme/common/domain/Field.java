package org.devcourse.resumeme.common.domain;

public enum Field implements DocsEnumType {
    FINANCE("금융/핀테크"),
    MANUFACTURE("제조"),
    COMMERCE("이커머스"),
    MEDIA("미디어"),
    AD("광고"),
    GAME("게임"),
    SOLUTION("솔루션/유틸리티"),
    SNS("커뮤니티/SNS")
    ;

    private final String description;

    Field(String fieldName) {
        this.description = fieldName;
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
