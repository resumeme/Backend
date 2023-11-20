package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.devcourse.resumeme.business.resume.domain.Property.DESCRIPTION;
import static org.devcourse.resumeme.business.resume.domain.Property.END_DATE;
import static org.devcourse.resumeme.business.resume.domain.Property.LINK;
import static org.devcourse.resumeme.business.resume.domain.Property.TITLE;
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
        this(component.get(TITLE.name()), LocalDate.parse(component.get(TITLE.startDate())), LocalDateUtils.parse(component.get(TITLE.endDate())),
                component.get(LINK.name()), component.get(DESCRIPTION.name()));
    }

    @Override
    public Component of(Long resumeId) {
        Component link = new Component(LINK, this.link, resumeId);
        Component description = new Component(DESCRIPTION, this.description, resumeId);

        return new Component(TITLE, activityName, startDate, endDate, resumeId, List.of(link, description));
    }

}
