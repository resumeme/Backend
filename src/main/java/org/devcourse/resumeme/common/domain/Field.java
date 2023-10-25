package org.devcourse.resumeme.common.domain;

public enum Field implements DocsEnumType {
    FINANCE("금융"),
    MANUFACTURE("제조"),
    RETAIL("유통"),
    MEDIA("미디어");

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
