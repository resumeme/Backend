package org.devcourse.resumeme.business.resume.controller.dto.v2;

import lombok.Getter;
import lombok.Setter;
import org.devcourse.resumeme.business.resume.controller.dto.career.CareerCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.certification.CertificationCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.activity.ActivityCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.language.ForeignLanguageCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.project.ProjectCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.link.ResumeLinkRequest;
import org.devcourse.resumeme.business.resume.controller.dto.training.TrainingCreateRequest;
import org.devcourse.resumeme.business.resume.domain.Property;
import org.devcourse.resumeme.business.resume.service.v2.ComponentDomain;

@Setter @Getter
public class ComponentCreateRequestV2 {

    private ActivityCreateRequest activities;

    private CareerCreateRequest careers;

    private CertificationCreateRequest certifications;

    private ForeignLanguageCreateRequest foreignLanguages;

    private ProjectCreateRequest projects;

    private TrainingCreateRequest trainings;

    private ResumeLinkRequest links;

    public ComponentCreateRequestV2() {
    }

    public ComponentDomain toVo(Property type) {
        return switch (type) {
            case ACTIVITIES -> activities.toVo();
            case CAREERS -> careers.toVo();
            case CERTIFICATIONS -> certifications.toVo();
            case FOREIGNLANGUAGES -> foreignLanguages.toVo();
            case PROJECTS -> projects.toVo();
            case TRAININGS -> trainings.toVo();
            case LINKS -> links.toVo();
            default -> throw new IllegalArgumentException();
        };
    }

}
