package org.devcourse.resumeme.business.resume.controller.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.Converter;
import org.devcourse.resumeme.business.resume.domain.ForeignLanguage;
import org.devcourse.resumeme.business.resume.entity.Component;

@Data
@NoArgsConstructor
@JsonTypeName("foreign-languages")
public class ForeignLanguageResponse extends ComponentResponse {

    private String language;

    private String examName;

    private String scoreOrGrade;

    public ForeignLanguageResponse(String type, Component component) {
        super(type, component);
        ForeignLanguage foreignLanguage = new ForeignLanguage(Converter.from(component));
        this.language = foreignLanguage.getLanguage();
        this.examName = foreignLanguage.getExamName();
        this.scoreOrGrade = foreignLanguage.getScoreOrGrade();
    }

}
