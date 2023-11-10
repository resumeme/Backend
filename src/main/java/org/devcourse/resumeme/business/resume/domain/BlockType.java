package org.devcourse.resumeme.business.resume.domain;

import org.devcourse.resumeme.business.resume.controller.career.dto.CareerCreateRequest;
import org.devcourse.resumeme.business.resume.controller.career.dto.CareerResponse;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.controller.certification.dto.CertificationCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ActivityCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ActivityResponse;
import org.devcourse.resumeme.business.resume.controller.dto.CertificationResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ForeignLanguageCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ForeignLanguageResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ProjectCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ProjectResponse;
import org.devcourse.resumeme.business.resume.controller.dto.TrainingCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.TrainingResponse;
import org.devcourse.resumeme.business.resume.domain.career.Career;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.common.domain.DocsEnumType;
import org.devcourse.resumeme.global.exception.CustomException;

import java.util.Arrays;
import java.util.function.Function;

public enum BlockType implements DocsEnumType {

    ACTIVITY("활동", "activities", ActivityCreateRequest.class, component -> new ActivityResponse(Activity.from(component))),
    CAREER("업무경험", "careers", CareerCreateRequest.class, component -> new CareerResponse(Career.from(component))),
    CERTIFICATION("수상 및 자격증", "certifications", CertificationCreateRequest.class, component -> new CertificationResponse(Certification.from(component))),
    FOREIGN_LANGUAGE("외국어", "foreign-languages", ForeignLanguageCreateRequest.class, component -> new ForeignLanguageResponse(ForeignLanguage.from(component))),
    PROJECT("프로젝트", "projects", ProjectCreateRequest.class, component -> new ProjectResponse(Project.from(component))),
    TRAINING("교육", "trainings", TrainingCreateRequest.class, component -> new TrainingResponse(Training.from(component)));

    private final String description;

    private final String urlParameter;

    private final Class<? extends ComponentCreateRequest> classType;

    public final Function<Component, ComponentResponse> from;

    BlockType(String description, String urlParameter, Class<? extends ComponentCreateRequest> classType, Function<Component, ComponentResponse> from) {
        this.description = description;
        this.urlParameter = urlParameter;
        this.classType = classType;
        this.from = from;
    }

    @Override
    public String getType() {
        return urlParameter;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public Class<?> getClassType() {
        return classType;
    }

    public String getUrlParameter() {
        return urlParameter;
    }

    public static BlockType of(String urlParameter) {
        return Arrays.stream(values())
                .filter(type -> type.urlParameter.equals(urlParameter))
                .findFirst()
                .orElseThrow(() -> new CustomException("", ""));
    }

    public static ComponentResponse from(String type, Component component) {
        return of(type).from.apply(component);
    }
}
