package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Getter;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.Converter;
import org.devcourse.resumeme.business.resume.domain.ForeignLanguage;

@Getter
public class ForeignLanguageResponse extends ComponentResponse {

    private String language;

    private String examName;

    private String scoreOrGrade;

    public ForeignLanguageResponse(Converter converter) {
        super(converter);
        ForeignLanguage foreignLanguage = (ForeignLanguage) converter;
        this.language = foreignLanguage.getLanguage();
        this.examName = foreignLanguage.getExamName();
        this.scoreOrGrade = foreignLanguage.getScoreOrGrade();
    }

}
