package org.devcourse.resumeme.business.resume.controller.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.devcourse.resumeme.business.resume.domain.ForeignLanguage;
import org.devcourse.resumeme.business.resume.service.vo.ForeignLanguageDomainVo;

@Getter @Setter
@NoArgsConstructor
@JsonTypeName("foreignLanguages")
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
    public ForeignLanguageDomainVo toVo() {
        ForeignLanguage foreignLanguage = new ForeignLanguage(language, examName, scoreOrGrade);

        return new ForeignLanguageDomainVo(foreignLanguage);
    }

}
