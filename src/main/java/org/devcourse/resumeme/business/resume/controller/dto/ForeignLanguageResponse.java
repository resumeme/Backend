package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.ForeignLanguage;

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
