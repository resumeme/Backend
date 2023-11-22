package org.devcourse.resumeme.business.resume.controller.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.Activity;
import org.devcourse.resumeme.business.resume.domain.Converter;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@JsonTypeName("activities")
public class ActivityResponse extends ComponentResponse {

    private String activityName;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean inProgress;

    private String link;

    private String description;

    public ActivityResponse(String type, Component component) {
        super(type, component);
        Activity activity = new Activity(Converter.from(component));
        this.activityName = activity.getActivityName();
        this.startDate = activity.getStartDate();
        this.endDate = activity.getEndDate();
        this.inProgress = activity.getEndDate() == null;
        this.link = activity.getLink();
        this.description = activity.getDescription();
    }

}
