package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.ForeignLanguage;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class ForeignLanguageResponse extends ComponentResponse {

    private String language;

    private String examName;

    private String scoreOrGrade;

    public ForeignLanguageResponse(ForeignLanguage foreignLanguage) {
        super(foreignLanguage);
        this.language = foreignLanguage.getLanguage();
        this.examName = foreignLanguage.getExamName();
        this.scoreOrGrade = foreignLanguage.getScoreOrGrade();
    }

}
