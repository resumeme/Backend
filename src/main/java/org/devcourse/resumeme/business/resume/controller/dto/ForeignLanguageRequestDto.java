package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.ForeignLanguage;

public record ForeignLanguageRequestDto(
        String language,
        String examName,
        String scoreOrGrade
) {
    public ForeignLanguage toEntity() {
        return new ForeignLanguage(language, examName, scoreOrGrade);
    }
}
