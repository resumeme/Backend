package org.devcourse.resumeme.business.resume.service.v2;

import lombok.Builder;
import lombok.Getter;
import org.devcourse.resumeme.business.resume.domain.Property;
import org.devcourse.resumeme.business.resume.domain.activity.Activity;
import org.devcourse.resumeme.business.resume.domain.certification.Certification;
import org.devcourse.resumeme.business.resume.domain.language.ForeignLanguage;
import org.devcourse.resumeme.business.resume.domain.project.Project;
import org.devcourse.resumeme.business.resume.domain.link.ReferenceLink;
import org.devcourse.resumeme.business.resume.domain.training.Training;
import org.devcourse.resumeme.business.resume.domain.career.Career;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static org.devcourse.resumeme.business.resume.domain.Property.ACTIVITIES;
import static org.devcourse.resumeme.business.resume.domain.Property.CAREERS;
import static org.devcourse.resumeme.business.resume.domain.Property.CERTIFICATIONS;
import static org.devcourse.resumeme.business.resume.domain.Property.FOREIGNLANGUAGES;
import static org.devcourse.resumeme.business.resume.domain.Property.LINKS;
import static org.devcourse.resumeme.business.resume.domain.Property.PROJECTS;
import static org.devcourse.resumeme.business.resume.domain.Property.TRAININGS;

@Getter
public class ResumeTemplate {

    private List<Activity> activity;

    private List<Career> career;

    private List<Certification> certification;

    private List<ForeignLanguage> foreignLanguage;

    private List<Project> project;

    private List<Training> training;

    private List<ReferenceLink> referenceLink;

    @Builder
    public ResumeTemplate(List<Activity> activity, List<Career> career,
            List<Certification> certification, List<ForeignLanguage> foreignLanguage,
            List<Project> project, List<Training> training, List<ReferenceLink> referenceLink) {
        this.activity = activity;
        this.career = career;
        this.certification = certification;
        this.foreignLanguage = foreignLanguage;
        this.project = project;
        this.training = training;
        this.referenceLink = referenceLink;
    }

    public static ResumeTemplate from(List<Component> components) {
        Map<Property, Component> response = components.stream()
                .collect(toMap(Component::getProperty, Function.identity()));

        return ResumeTemplate.builder()
                .activity(Activity.of(response.get(ACTIVITIES)))
                .career(Career.of(response.get(CAREERS)))
                .certification(Certification.of(response.get(CERTIFICATIONS)))
                .foreignLanguage(ForeignLanguage.of(response.get(FOREIGNLANGUAGES)))
                .project(Project.of(response.get(PROJECTS)))
                .training(Training.of(response.get(TRAININGS)))
                .referenceLink(ReferenceLink.of(response.get(LINKS)))
                .build();
    }

}
