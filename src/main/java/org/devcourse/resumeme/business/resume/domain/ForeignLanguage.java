package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.util.List;
import java.util.Map;

import static org.devcourse.resumeme.common.util.Validator.notNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ForeignLanguage implements Converter {

    private String language;

    private String examName;

    private String scoreOrGrade;

    public ForeignLanguage(String language, String examName, String scoreOrGrade) {
        notNull(language);
        notNull(examName);
        notNull(scoreOrGrade);

        this.language = language;
        this.examName = examName;
        this.scoreOrGrade = scoreOrGrade;
    }

    public ForeignLanguage(Map<String, String> component) {
        this(component.get("language"), component.get("examName"), component.get("scoreOrGrade"));
    }

    @Override
    public Component of(Long resumeId) {
        Component examName = new Component("examName", this.examName, null, null, resumeId, null);
        Component scoreOrGrade = new Component("scoreOrGrade", this.scoreOrGrade, null, null, resumeId, null);

        return new Component("language", this.language, null, null, resumeId, List.of(examName, scoreOrGrade));
    }

}
