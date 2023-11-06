package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.resume.ForeignLanguage;

public record ForeignLanguageResponse(
        String language,
        String examName,
        String scoreOrGrade
) {
    public ForeignLanguageResponse(ForeignLanguage foreignLanguage) {
        this(
                foreignLanguage.getLanguage(),
                foreignLanguage.getExamName(),
                foreignLanguage.getScoreOrGrade()
        );
    }
}
