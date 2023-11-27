package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Getter;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.Activity;
import org.devcourse.resumeme.business.resume.domain.Converter;

import java.time.LocalDate;

@Getter
public class ActivityResponse extends ComponentResponse {

    private String activityName;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean inProgress;

    private String link;

    private String description;

    public ActivityResponse(Converter converter) {
        super(converter);
        Activity activity = (Activity) converter;
        this.activityName = activity.getActivityName();
        this.startDate = activity.getStartDate();
        this.endDate = activity.getEndDate();
        this.inProgress = activity.getEndDate() == null;
        this.link = activity.getLink();
        this.description = activity.getDescription();
    }

}
