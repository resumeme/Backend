package org.devcourse.resumeme.business.resume.domain;

import org.devcourse.resumeme.business.resume.controller.career.dto.CareerResponse;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ActivityResponse;
import org.devcourse.resumeme.business.resume.controller.dto.CertificationResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ForeignLanguageResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ProjectResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeLinkResponse;
import org.devcourse.resumeme.business.resume.controller.dto.TrainingResponse;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.common.domain.DocsEnumType;
import org.devcourse.resumeme.global.exception.CustomException;

import java.util.Arrays;
import java.util.function.Function;

public enum BlockType implements DocsEnumType {

    ACTIVITY("활동", "activities", ActivityResponse::new),
    CAREER("업무경험", "careers", CareerResponse::new),
    CERTIFICATION("수상 및 자격증", "certifications", CertificationResponse::new),
    FOREIGN_LANGUAGE("외국어", "foreignLanguages", ForeignLanguageResponse::new),
    PROJECT("프로젝트", "projects", ProjectResponse::new),
    TRAINING("교육", "trainings", TrainingResponse::new),
    LINK("외부 링크", "links", ResumeLinkResponse::new);

    private final String description;

    private final String urlParameter;

    public final Function<Component, ComponentResponse> from;

    BlockType(String description, String urlParameter, Function<Component, ComponentResponse> from) {
        this.description = description;
        this.urlParameter = urlParameter;
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

    public String getUrlParameter() {
        return urlParameter;
    }

    public static BlockType of(String urlParameter) {
        return Arrays.stream(values())
                .filter(type -> type.urlParameter.equals(urlParameter))
                .findFirst()
                .orElseThrow(() -> new CustomException("", ""));
    }

    public static ComponentResponse convert(String type, Component component) {
        return of(type).from.apply(component);
    }
}
