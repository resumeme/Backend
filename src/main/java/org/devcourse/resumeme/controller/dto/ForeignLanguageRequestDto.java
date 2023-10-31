package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.resume.ForeignLanguage;
import org.devcourse.resumeme.domain.resume.Resume;

public record ForeignLanguageRequestDto(
        String language,
        String examName,
        String scoreOrGrade
) {
    public ForeignLanguage toEntity(Resume resume) {
        return new ForeignLanguage(language, examName, scoreOrGrade, resume);
    }
}

