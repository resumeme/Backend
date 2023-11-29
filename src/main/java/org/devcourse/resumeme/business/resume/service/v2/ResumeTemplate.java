package org.devcourse.resumeme.business.resume.service.v2;

import lombok.Builder;
import lombok.Getter;
import org.devcourse.resumeme.business.resume.domain.activity.Activity;
import org.devcourse.resumeme.business.resume.domain.certification.Certification;
import org.devcourse.resumeme.business.resume.domain.language.ForeignLanguage;
import org.devcourse.resumeme.business.resume.domain.project.Project;
import org.devcourse.resumeme.business.resume.domain.link.ReferenceLink;
import org.devcourse.resumeme.business.resume.domain.training.Training;
import org.devcourse.resumeme.business.resume.domain.career.Career;

import java.util.List;

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

}
