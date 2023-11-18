package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.devcourse.resumeme.common.util.Validator.notNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Activity implements Converter {

    private String activityName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String link;

    private String description;

    public Activity(String activityName, LocalDate startDate, LocalDate endDate, String link, String description) {
        notNull(activityName);
        notNull(startDate);

        this.activityName = activityName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.link = link;
        this.description = description;
    }

    public Activity(Map<String, String> component) {
        this(component.get("title"), LocalDate.parse(component.get("titleStartDate")),
                LocalDate.parse(component.get("titleEndDate")), component.get("link"), component.get("description"));
    }

    @Override
    public Component of(Long resumeId) {
        Component link = new Component("link", this.link, null, null, resumeId, null);
        Component description = new Component("description", this.description, null, null, resumeId, null);

        return new Component("title", activityName, startDate, endDate, resumeId, List.of(link, description));
    }

}
