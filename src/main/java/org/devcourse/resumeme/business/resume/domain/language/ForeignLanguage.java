package org.devcourse.resumeme.business.resume.domain.language;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.ComponentInfo;
import org.devcourse.resumeme.business.resume.domain.Property;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.devcourse.resumeme.business.resume.domain.Property.EXAM_NAME;
import static org.devcourse.resumeme.business.resume.domain.Property.LANGUAGE;
import static org.devcourse.resumeme.business.resume.domain.Property.SCORE;
import static org.devcourse.resumeme.common.util.Validator.notNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ForeignLanguage {

    private String language;

    private String examName;

    private String scoreOrGrade;

    private ComponentInfo componentInfo;

    public ForeignLanguage(String language, String examName, String scoreOrGrade) {
        notNull(language);
        notNull(examName);
        notNull(scoreOrGrade);

        this.language = language;
        this.examName = examName;
        this.scoreOrGrade = scoreOrGrade;
    }

    @Builder
    private ForeignLanguage(String language, String examName, String scoreOrGrade, Component component) {
        notNull(language);
        notNull(examName);
        notNull(scoreOrGrade);

        this.language = language;
        this.examName = examName;
        this.scoreOrGrade = scoreOrGrade;
        this.componentInfo = new ComponentInfo(component);
    }

    public Component toComponent(Long resumeId) {
        Component examName = new Component(EXAM_NAME, this.examName, resumeId);
        Component scoreOrGrade = new Component(SCORE, this.scoreOrGrade, resumeId);

        return new Component(LANGUAGE, this.language, null, null, resumeId, List.of(examName, scoreOrGrade));
    }

    public static List<ForeignLanguage> of(Component component) {
        if (component == null) {
            return new ArrayList<>();
        }

        return component.getComponents().stream()
                .map(ForeignLanguageConverter::of)
                .map(ForeignLanguage::of)
                .toList();
    }

    private static ForeignLanguage of(ForeignLanguageConverter converter) {
        return ForeignLanguage.builder()
                .component(converter.foreignLanguage)
                .language(converter.foreignLanguage.getContent())
                .examName(converter.details.examName.getContent())
                .scoreOrGrade(converter.details.scoreOrGrade.getContent())
                .build();
    }

    @Builder
    private static class ForeignLanguageConverter {

        private Component foreignLanguage;

        private ForeignLanguageDetails details;

        @Builder
        private static class ForeignLanguageDetails {

            private Component examName;

            private Component scoreOrGrade;

            public static ForeignLanguageDetails of(Component component) {
                Map<Property, Component> componentMap = component.getComponents().stream()
                        .collect(toMap(Component::getProperty, identity()));

                return ForeignLanguageDetails.builder()
                        .examName(componentMap.get(EXAM_NAME))
                        .scoreOrGrade(componentMap.get(SCORE))
                        .build();
            }

        }

        private static ForeignLanguageConverter of(Component component) {
            return ForeignLanguageConverter.builder()
                    .foreignLanguage(component)
                    .details(ForeignLanguageDetails.of(component))
                    .build();
        }

    }

}
