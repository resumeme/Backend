package org.devcourse.resumeme.business.resume.controller.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.devcourse.resumeme.business.resume.domain.ForeignLanguage;

@Getter @Setter
@NoArgsConstructor
@JsonTypeName("foreign-languages")
public class ForeignLanguageCreateRequest extends ComponentCreateRequest {

    private String language;

    private String examName;

    private String scoreOrGrade;

    public ForeignLanguageCreateRequest(
            String language,
            String examName,
            String scoreOrGrade
    ) {
        this.language = language;
        this.examName = examName;
        this.scoreOrGrade = scoreOrGrade;
    }

    @Override
    public ForeignLanguage toEntity() {
        return new ForeignLanguage(language, examName, scoreOrGrade);
    }

}
