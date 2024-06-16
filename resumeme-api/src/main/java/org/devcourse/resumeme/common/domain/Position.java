package org.devcourse.resumeme.common.domain;

public enum Position implements DocsEnumType {
    FULLSTACK("풀스택"),
    FRONT("프론트엔드"),
    BACK("벡엔드"),
    MOBILE("안드로이드/IOS"),
    DEVOPS("데브옵스"),
    ML_AI("인공지능/머신러닝")
    ;

    private final String description;

    Position(String description) {
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
