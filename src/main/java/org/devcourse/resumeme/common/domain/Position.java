package org.devcourse.resumeme.common.domain;

public enum Position implements DocsEnumType {
    FRONT("프론트엔드"),
    BACK("벡엔드"),
    MOBILE("안드로이드/IOS"),
    DEVOPS("데브옵스");

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
