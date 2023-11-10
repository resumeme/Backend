package org.devcourse.resumeme.business.resume.domain;

import org.devcourse.resumeme.business.resume.controller.career.dto.CareerCreateRequest;
import org.devcourse.resumeme.business.resume.controller.certification.dto.CertificationCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ActivityCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ForeignLanguageCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.ProjectCreateRequest;
import org.devcourse.resumeme.business.resume.controller.dto.TrainingCreateRequest;
import org.devcourse.resumeme.common.domain.DocsEnumType;
import org.devcourse.resumeme.global.exception.CustomException;

import java.util.Arrays;

public enum BlockType implements DocsEnumType {

    ACTIVITY("활동", "activities", ActivityCreateRequest.class),
    CAREER("업무경험", "careers", CareerCreateRequest.class),
    CERTIFICATION("수상 및 자격증", "certifications", CertificationCreateRequest.class),
    FOREIGN_LANGUAGE("외국어", "foreign-languages", ForeignLanguageCreateRequest.class),
    PROJECT("프로젝트", "projects", ProjectCreateRequest.class),
    TRAINING("교육", "trainings", TrainingCreateRequest.class);

    private final String description;

    private final String urlParameter;

    private final Class<? extends ComponentCreateRequest> classType;

    BlockType(String description, String urlParameter, Class<? extends ComponentCreateRequest> classType) {
        this.description = description;
        this.urlParameter = urlParameter;
        this.classType = classType;
    }

    @Override
    public String getType() {
        return name();
    }

    @Override
    public String getDescription() {
        return description;
    }

    public Class<?> getClassType() {
        return classType;
    }

    public static BlockType of(String urlParameter) {
        return Arrays.stream(values())
                .filter(type -> type.urlParameter.equals(urlParameter))
                .findFirst()
                .orElseThrow(() -> new CustomException("", ""));
    }

}
