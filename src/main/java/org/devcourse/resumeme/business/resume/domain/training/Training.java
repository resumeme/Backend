package org.devcourse.resumeme.business.resume.domain.training;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.ComponentInfo;
import org.devcourse.resumeme.business.resume.domain.Property;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Double.parseDouble;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.devcourse.resumeme.business.resume.domain.Property.DEGREE;
import static org.devcourse.resumeme.business.resume.domain.Property.DESCRIPTION;
import static org.devcourse.resumeme.business.resume.domain.Property.MAJOR;
import static org.devcourse.resumeme.business.resume.domain.Property.MAX_SCORE;
import static org.devcourse.resumeme.business.resume.domain.Property.SCORE;
import static org.devcourse.resumeme.business.resume.domain.Property.TRAINING;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Training {

    private String explanation;

    private EducationalDetails educationalDetails;

    private DateDetails dateDetails;

    private GPADetails gpaDetails;

    private ComponentInfo componentInfo;

    public Training(String organization, String major, String degree, LocalDate admissionDate,
            LocalDate graduationDate, double gpa, double maxGpa, String explanation) {
        this.educationalDetails = new EducationalDetails(organization, major, degree);
        this.dateDetails = new DateDetails(admissionDate, graduationDate);
        this.gpaDetails = new GPADetails(gpa, maxGpa);
        this.explanation = explanation;
    }

    @Builder
    private Training(String organization, String major, String degree, LocalDate admissionDate,
            LocalDate graduationDate, double gpa, double maxGpa, String explanation, Component component) {
        this.educationalDetails = new EducationalDetails(organization, major, degree);
        this.dateDetails = new DateDetails(admissionDate, graduationDate);
        this.gpaDetails = new GPADetails(gpa, maxGpa);
        this.explanation = explanation;
        this.componentInfo = new ComponentInfo(component);
    }

    public Component toComponent(Long resumeId) {
        Component explanation = new Component(DESCRIPTION, this.explanation, resumeId);
        Component degree = new Component(DEGREE, educationalDetails.getDegree(), resumeId);
        Component major = new Component(MAJOR, educationalDetails.getMajor(), resumeId);
        Component gpa = new Component(SCORE, String.valueOf(gpaDetails.getGpa()), resumeId);
        Component maxGpa = new Component(MAX_SCORE, String.valueOf(gpaDetails.getMaxGpa()), resumeId);

        return new Component(TRAINING, this.educationalDetails.getOrganization(), this.dateDetails.getAdmissionDate(), this.dateDetails.getGraduationDate(),
                resumeId, List.of(explanation, degree, major, gpa, maxGpa));
    }

    public static List<Training> of(Component component) {
        if (component == null) {
            return new ArrayList<>();
        }

        return component.getComponents().stream()
                .map(TrainingConverter::of)
                .map(Training::of)
                .toList();
    }

    private static Training of(TrainingConverter converter) {
        return Training.builder()
                .component(converter.training)
                .organization(converter.training.getContent())
                .admissionDate(converter.training.getStartDate())
                .graduationDate(converter.training.getEndDate())
                .major(converter.details.major.getContent())
                .degree(converter.details.degree.getContent())
                .gpa(parseDouble(converter.details.gpa.getContent()))
                .maxGpa(parseDouble(converter.details.maxGpa.getContent()))
                .explanation(converter.details.explanation.getContent())
                .build();
    }

    @Builder
    private static class TrainingConverter {

        private Component training;

        private TrainingDetails details;

        @Builder
        private static class TrainingDetails {

            private Component explanation;

            private Component degree;

            private Component major;

            private Component gpa;

            private Component maxGpa;

            public static TrainingDetails of(Component component) {
                Map<Property, Component> componentMap = component.getComponents().stream()
                        .collect(toMap(Component::getProperty, identity()));

                return TrainingDetails.builder()
                        .explanation(componentMap.get(DESCRIPTION))
                        .degree(componentMap.get(DEGREE))
                        .major(componentMap.get(MAJOR))
                        .gpa(componentMap.get(SCORE))
                        .maxGpa(componentMap.get(MAX_SCORE))
                        .build();
            }

        }

        private static TrainingConverter of(Component component) {
            return TrainingConverter.builder()
                    .training(component)
                    .details(TrainingDetails.of(component))
                    .build();
        }

    }

}
