package org.devcourse.resumeme.business.resume.controller.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.devcourse.resumeme.business.resume.controller.career.dto.CareerCreateRequest;
import org.devcourse.resumeme.business.resume.controller.certification.dto.CertificationCreateRequest;
import org.devcourse.resumeme.business.resume.domain.Converter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = ActivityCreateRequest.class, name = "activities"),
        @JsonSubTypes.Type(value = CareerCreateRequest.class, name = "careers"),
        @JsonSubTypes.Type(value = CertificationCreateRequest.class, name = "certifications"),
        @JsonSubTypes.Type(value = ForeignLanguageCreateRequest.class, name = "foreign-languages"),
        @JsonSubTypes.Type(value = ProjectCreateRequest.class, name = "projects"),
        @JsonSubTypes.Type(value = TrainingCreateRequest.class, name = "trainings")
})
public abstract class ComponentCreateRequest {

    public abstract Converter toEntity();

}
