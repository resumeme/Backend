package org.devcourse.resumeme.business.resume.controller.dto.activity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentCreateRequest;
import org.devcourse.resumeme.business.resume.domain.activity.Activity;
import org.devcourse.resumeme.business.resume.service.vo.ActivityDomainVo;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ActivityCreateRequest extends ComponentCreateRequest {

    private String activityName;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean inProgress;

    private String link;

    private String description;

    public ActivityCreateRequest(
            String activityName,
            LocalDate startDate,
            LocalDate endDate,
            boolean inProgress,
            String link,
            String description
    ) {
        this.activityName = activityName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.inProgress = inProgress;
        this.link = link;
        this.description = description;
    }

    @Override
    public ActivityDomainVo toVo() {
        Activity activity = new Activity(activityName, startDate, endDate, link, description);

        return new ActivityDomainVo(activity);
    }

}
