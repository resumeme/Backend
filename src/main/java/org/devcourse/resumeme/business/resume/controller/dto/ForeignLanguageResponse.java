package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.ForeignLanguage;

@Data
public class ForeignLanguageResponse extends ComponentResponse {

    private String language;

    private String examName;

    private String scoreOrGrade;

    public ForeignLanguageResponse(
            Long id, String language,
            String examName,
            String scoreOrGrade
    ) {
        super(id);
        this.language = language;
        this.examName = examName;
        this.scoreOrGrade = scoreOrGrade;
    }

    public ForeignLanguageResponse(ForeignLanguage foreignLanguage, Long id) {
        this(
                id,
                foreignLanguage.getLanguage(),
                foreignLanguage.getExamName(),
                foreignLanguage.getScoreOrGrade()
        );
    }

}
