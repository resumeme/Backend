package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.ForeignLanguage;
import org.devcourse.resumeme.business.resume.entity.Component;

@Data
public class ForeignLanguageResponse extends ComponentResponse {

    private String language;

    private String examName;

    private String scoreOrGrade;

    public ForeignLanguageResponse(ForeignLanguage foreignLanguage, Component component) {
        super(component);
        this.language = foreignLanguage.getLanguage();
        this.examName = foreignLanguage.getExamName();
        this.scoreOrGrade = foreignLanguage.getScoreOrGrade();
    }

}
