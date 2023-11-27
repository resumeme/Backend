package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;
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
public class Activity implements Converter {

    private String activityName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String link;

    private String description;

    @Builder
    public Activity(String activityName, LocalDate startDate, LocalDate endDate, String link, String description) {
        notNull(activityName);
        notNull(startDate);

        this.activityName = activityName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.link = link;
        this.description = description;
    }

    @Override
    public Component toComponent(Long resumeId) {
        Component link = new Component(LINK, this.link, resumeId);
        Component description = new Component(DESCRIPTION, this.description, resumeId);

        return new Component(TITLE, activityName, startDate, endDate, resumeId, List.of(link, description));
    }

    public static Activity of(List<Component> components) {
        ActivityConverter converter = ActivityConverter.of(components);

        return Activity.of(converter);
    }

    private static Activity of(ActivityConverter converter) {
        return Activity.builder()
                .activityName(converter.activity.getContent())
                .startDate(converter.activity.getStartDate())
                .endDate(converter.activity.getEndDate())
                .link(converter.activityName.link.getContent())
                .description(converter.activityName.description.getContent())
                .build();
    }

    @Builder
    private static class ActivityConverter {

        private Component activity;

        private ActivityNameComponent activityName;

        @Builder
        private static class ActivityNameComponent {

            private Component link;

            private Component description;

        }

        private static ActivityConverter of(List<Component> components) {
            Map<Property, Component> componentMap = components.stream()
                    .collect(toMap(Component::getProperty, identity()));

            ActivityNameComponent activityName = ActivityNameComponent.builder()
                    .link(requireNonNull(componentMap.get(LINK)))
                    .description(requireNonNull(componentMap.get(DESCRIPTION)))
                    .build();

            return ActivityConverter.builder()
                    .activityName(activityName)
                    .activity(requireNonNull(componentMap.get(TITLE)))
                    .build();
        }
    }

}
