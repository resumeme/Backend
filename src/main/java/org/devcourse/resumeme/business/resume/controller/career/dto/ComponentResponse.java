package org.devcourse.resumeme.business.resume.controller.career.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ActivityResponse;
import org.devcourse.resumeme.business.resume.controller.dto.CertificationResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ForeignLanguageResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ProjectResponse;
import org.devcourse.resumeme.business.resume.controller.dto.ResumeLinkResponse;
import org.devcourse.resumeme.business.resume.controller.dto.TrainingResponse;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDateTime;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = ActivityResponse.class, name = "activities"),
        @JsonSubTypes.Type(value = CareerResponse.class, name = "careers"),
        @JsonSubTypes.Type(value = CertificationResponse.class, name = "certifications"),
        @JsonSubTypes.Type(value = ForeignLanguageResponse.class, name = "foreign-languages"),
        @JsonSubTypes.Type(value = ProjectResponse.class, name = "projects"),
        @JsonSubTypes.Type(value = TrainingResponse.class, name = "trainings"),
        @JsonSubTypes.Type(value = ResumeLinkResponse.class, name = "links")
})
@NoArgsConstructor
public abstract class ComponentResponse {

    @Getter
    protected String type;

    @Getter
    protected Long componentId;

    @Getter
    protected Long originComponentId;

    @Getter
    protected boolean reflectFeedback;

    @Getter
    private LocalDateTime createdDate;

    protected ComponentResponse(String type, Component component) {
        this.type = type;
        this.componentId = component.getId();
        this.originComponentId = component.getOriginComponentId();
        this.reflectFeedback = component.isReflectFeedBack();
        this.createdDate = component.getCreatedDate();
    }

}
