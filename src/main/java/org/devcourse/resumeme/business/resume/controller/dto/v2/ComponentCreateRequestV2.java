package org.devcourse.resumeme.business.resume.controller.dto.v2;

import lombok.Getter;
import lombok.Setter;
import org.devcourse.resumeme.business.resume.controller.career.dto.CareerCreateRequest;
import org.devcourse.resumeme.business.resume.controller.certification.dto.CertificationCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ActivityCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ForeignLanguageCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ProjectCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeLinkRequest;
import org.devcourse.resumeme.business.resume.controller.dto.TrainingCreateRequest;
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
