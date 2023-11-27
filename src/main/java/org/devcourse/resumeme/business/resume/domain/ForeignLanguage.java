package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

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
public class ForeignLanguage extends Converter {

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

    @Builder
    private ForeignLanguage(String language, String examName, String scoreOrGrade, Component component) {
        super(component);
        notNull(language);
        notNull(examName);
        notNull(scoreOrGrade);

        this.language = language;
        this.examName = examName;
        this.scoreOrGrade = scoreOrGrade;
    }

    @Override
    public Component toComponent(Long resumeId) {
        Component examName = new Component(EXAM_NAME, this.examName, resumeId);
        Component scoreOrGrade = new Component(SCORE, this.scoreOrGrade, resumeId);

        return new Component(LANGUAGE, this.language, null, null, resumeId, List.of(examName, scoreOrGrade));
    }

    public static ForeignLanguage of(List<Component> components) {
        ForeignLanguageConverter converter = ForeignLanguageConverter.of(components);

        return ForeignLanguage.of(converter);
    }

    private static ForeignLanguage of(ForeignLanguageConverter converter) {
        return ForeignLanguage.builder()
                .component(converter.foreignLanguage)
                .language(converter.foreignLanguage.getContent())
                .examName(converter.details.examName.getContent())
                .scoreOrGrade(converter.details.scoreOrGrade.getContent())
                .build();
    }

    @Data
    @Builder
    private static class ForeignLanguageConverter {

        private Component foreignLanguage;

        private ForeignLanguageDetails details;

        @Data
        @Builder
        private static class ForeignLanguageDetails {

            private Component examName;

            private Component scoreOrGrade;

        }

        public static ForeignLanguageConverter of(List<Component> components) {
            Map<Property, Component> componentMap = components.stream()
                    .collect(toMap(Component::getProperty, identity()));

            ForeignLanguageDetails details = ForeignLanguageDetails.builder()
                    .examName(componentMap.get(EXAM_NAME))
                    .scoreOrGrade(componentMap.get(SCORE))
                    .build();

            return ForeignLanguageConverter.builder()
                    .foreignLanguage(componentMap.get(LANGUAGE))
                    .details(details)
                    .build();
        }

    }

}
