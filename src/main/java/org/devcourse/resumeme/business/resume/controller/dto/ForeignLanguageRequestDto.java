package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.ForeignLanguage;
import org.devcourse.resumeme.business.resume.domain.Resume;

public record ForeignLanguageRequestDto(
        String language,
        String examName,
        String scoreOrGrade
) {
    public ForeignLanguage toEntity(Resume resume) {
        return new ForeignLanguage(language, examName, scoreOrGrade, resume);
    }
}
