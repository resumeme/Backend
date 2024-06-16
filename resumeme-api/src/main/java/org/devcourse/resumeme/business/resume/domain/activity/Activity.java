package org.devcourse.resumeme.business.resume.domain.activity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.ComponentInfo;
import org.devcourse.resumeme.business.resume.domain.Property;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.devcourse.resumeme.business.resume.domain.Property.DESCRIPTION;
import static org.devcourse.resumeme.business.resume.domain.Property.LINK;
import static org.devcourse.resumeme.business.resume.domain.Property.TITLE;
import static org.devcourse.resumeme.common.util.Validator.notNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Activity {

    private String activityName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String link;

    private String description;

    private ComponentInfo componentInfo;

    public Activity(String activityName, LocalDate startDate, LocalDate endDate, String link, String description) {
        notNull(activityName);
        notNull(startDate);

        this.activityName = activityName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.link = link;
        this.description = description;
    }

    @Builder
    private Activity(String activityName, LocalDate startDate, LocalDate endDate, String link, String description, Component component) {
        notNull(activityName);
        notNull(startDate);

        this.activityName = activityName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.link = link;
        this.description = description;
        this.componentInfo = new ComponentInfo(component);
    }

    public Component toComponent(Long resumeId) {
        Component link = new Component(LINK, this.link, resumeId);
        Component description = new Component(DESCRIPTION, this.description, resumeId);

        return new Component(TITLE, activityName, startDate, endDate, resumeId, List.of(link, description));
    }

    public static List<Activity> of(Component component) {
        if (component == null) {
            return new ArrayList<>();
        }

        return component.getComponents().stream()
                .map(ActivityConverter::of)
                .map(Activity::of)
                .toList();
    }

    private static Activity of(ActivityConverter converter) {
        return Activity.builder()
                .component(converter.activity)
                .activityName(converter.activity.getContent())
                .startDate(converter.activity.getStartDate())
                .endDate(converter.activity.getEndDate())
                .link(converter.details.link.getContent())
                .description(converter.details.description.getContent())
                .build();
    }

    @Builder
    private static class ActivityConverter {

        private Component activity;

        private ActivityDetails details;

        @Builder
        private static class ActivityDetails {

            private Component link;

            private Component description;

            private static ActivityDetails of(Component component) {
                Map<Property, Component> componentMap = component.getComponents().stream()
                        .collect(toMap(Component::getProperty, identity()));

                return ActivityDetails.builder()
                        .link(componentMap.get(LINK))
                        .description(componentMap.get(DESCRIPTION))
                        .build();
            }

        }

        private static ActivityConverter of(Component component) {
            return ActivityConverter.builder()
                    .activity(component)
                    .details(ActivityDetails.of(component))
                    .build();
        }

    }

}
