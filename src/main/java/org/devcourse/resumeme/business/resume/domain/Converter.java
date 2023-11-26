package org.devcourse.resumeme.business.resume.domain;

import org.devcourse.resumeme.business.resume.domain.model.Components;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static org.devcourse.resumeme.business.resume.domain.Property.DUTY;

public interface Converter {

    Component of(Long resumeId);

    static Components convert(Component component) {
        Map<Property, Component> components = flatComponents(component).stream()
                .collect(toMap(c -> Property.valueOf(c.getProperty()), Function.identity()));

        return new Components(components);
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
