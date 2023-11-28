package org.devcourse.resumeme.business.resume.service.v2;

import lombok.Builder;
import lombok.Getter;
import org.devcourse.resumeme.business.resume.domain.Activity;
import org.devcourse.resumeme.business.resume.domain.Certification;
import org.devcourse.resumeme.business.resume.domain.ForeignLanguage;
import org.devcourse.resumeme.business.resume.domain.Project;
import org.devcourse.resumeme.business.resume.domain.ReferenceLink;
import org.devcourse.resumeme.business.resume.domain.Training;
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
