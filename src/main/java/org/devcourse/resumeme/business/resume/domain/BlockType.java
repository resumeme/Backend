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
import org.devcourse.resumeme.business.resume.controller.dto.ResumeLinkRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeLinkResponse;
import org.devcourse.resumeme.business.resume.controller.dto.TrainingCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.TrainingResponse;
import org.devcourse.resumeme.business.resume.domain.career.Career;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.common.domain.DocsEnumType;
import org.devcourse.resumeme.global.exception.CustomException;

import java.util.Arrays;
import java.util.function.BiFunction;

public enum BlockType implements DocsEnumType {

    ACTIVITY("활동", "activities", ActivityCreateRequest.class, (type, component) -> new ActivityResponse(type, new Activity(Converter.from(component)), component)),
    CAREER("업무경험", "careers", CareerCreateRequest.class, (type, component) -> new CareerResponse(type, new Career(Converter.from(component)), component)),
    CERTIFICATION("수상 및 자격증", "certifications", CertificationCreateRequest.class, (type, component) -> new CertificationResponse(type, new Certification(Converter.from(component)), component)),
    FOREIGN_LANGUAGE("외국어", "foreign-languages", ForeignLanguageCreateRequest.class, (type, component) -> new ForeignLanguageResponse(type, new ForeignLanguage(Converter.from(component)), component)),
    PROJECT("프로젝트", "projects", ProjectCreateRequest.class, (type, component) -> new ProjectResponse(type, new Project(Converter.from(component)), component)),
    TRAINING("교육", "trainings", TrainingCreateRequest.class, (type, component) -> new TrainingResponse(type, new Training(Converter.from(component)), component)),
    LINK("외부 링크", "links", ResumeLinkRequest.class, (type, component) -> new ResumeLinkResponse(type, new ReferenceLink(Converter.from(component)), component));

    private final String description;

    private final String urlParameter;

    private final Class<? extends ComponentCreateRequest> classType;

    public final BiFunction<String, Component, ComponentResponse> from;

    BlockType(String description, String urlParameter, Class<? extends ComponentCreateRequest> classType, BiFunction<String, Component, ComponentResponse> from) {
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
        return of(type).from.apply(type, component);
    }
}
