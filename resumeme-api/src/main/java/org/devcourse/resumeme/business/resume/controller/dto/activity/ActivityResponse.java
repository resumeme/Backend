package org.devcourse.resumeme.business.resume.controller.dto.activity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.activity.Activity;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class ActivityResponse extends ComponentResponse {

    private String activityName;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean inProgress;

    private String link;

    private String description;

    public ActivityResponse(Activity activity) {
        super(activity.getComponentInfo());
        this.activityName = activity.getActivityName();
        this.startDate = activity.getStartDate();
        this.endDate = activity.getEndDate();
        this.inProgress = activity.getEndDate() == null;
        this.link = activity.getLink();
        this.description = activity.getDescription();
    }

}
