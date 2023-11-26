package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.util.List;
import java.util.Map;

import static org.devcourse.resumeme.business.resume.domain.Property.EXAM_NAME;
import static org.devcourse.resumeme.business.resume.domain.Property.LANGUAGE;
import static org.devcourse.resumeme.business.resume.domain.Property.SCORE;
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

    public ForeignLanguage(Map<Property, Component> components) {
        this.language = components.get(LANGUAGE).getContent();
        this.examName = components.get(EXAM_NAME).getContent();
        this.scoreOrGrade = components.get(SCORE).getContent();
    }

    @Override
    public Component of(Long resumeId) {
        Component examName = new Component(EXAM_NAME, this.examName, resumeId);
        Component scoreOrGrade = new Component(SCORE, this.scoreOrGrade, resumeId);

        return new Component(LANGUAGE, this.language, null, null, resumeId, List.of(examName, scoreOrGrade));
    }

}
