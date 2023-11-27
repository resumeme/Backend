package org.devcourse.resumeme.business.resume.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.business.resume.domain.Property.DUTY;

@NoArgsConstructor(access = PROTECTED)
public abstract class Converter {

    @Getter
    protected Long componentId;

    @Getter
    protected Long originComponentId;

    @Getter
    protected boolean reflectFeedback;

    public Converter(Component component) {
        this.componentId = component.getId();
        this.originComponentId = component.getOriginComponentId();
        this.reflectFeedback = component.isReflectFeedBack();
    }

    public abstract Component toComponent(Long resumeId);

    public static Converter of(Component component) {
        List<Component> components = flatComponents(component);

        return switch (component.getProperty()) {
            case ACTIVITIES -> Activity.of(components);
            default -> throw new IllegalStateException("Unexpected value: " + component.getProperty());
        };
    }

    private static List<Component> flatComponents(Component component) {
        if (component.isType(DUTY.name())) {
            return List.of(component);
        }

        List<Component> result = new ArrayList<>();

        result.add(component);
        for (Component subComponent : component.getComponents()) {
            if (subComponent.getComponents() != null) {
                result.addAll(flatComponents(subComponent));
            }
        }

        return result;
    }


}
