package org.devcourse.resumeme.business.resume.controller.dto.language;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.language.ForeignLanguage;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class ForeignLanguageResponse extends ComponentResponse {

    private String language;

    private String examName;

    private String scoreOrGrade;

    public ForeignLanguageResponse(ForeignLanguage foreignLanguage) {
        super(foreignLanguage.getComponentInfo());
        this.language = foreignLanguage.getLanguage();
        this.examName = foreignLanguage.getExamName();
        this.scoreOrGrade = foreignLanguage.getScoreOrGrade();
    }

}
