package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.ForeignLanguage;

@Data
public class ForeignLanguageResponse implements ComponentResponse {

    private String language;

    private String examName;

    private String scoreOrGrade;

    public ForeignLanguageResponse(
            String language,
            String examName,
            String scoreOrGrade
    ) {
        this.language = language;
        this.examName = examName;
        this.scoreOrGrade = scoreOrGrade;
    }

    public ForeignLanguageResponse(ForeignLanguage foreignLanguage) {
        this(
                foreignLanguage.getLanguage(),
                foreignLanguage.getExamName(),
                foreignLanguage.getScoreOrGrade()
        );
    }

}
