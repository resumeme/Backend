package org.devcourse.resumeme.business.resume.controller.dto.language;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentCreateRequest;
import org.devcourse.resumeme.business.resume.domain.language.ForeignLanguage;
import org.devcourse.resumeme.business.resume.service.vo.ForeignLanguageDomainVo;

@Getter
@NoArgsConstructor
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
