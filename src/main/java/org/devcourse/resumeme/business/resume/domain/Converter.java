package org.devcourse.resumeme.business.resume.domain;

import org.devcourse.resumeme.business.resume.entity.Component;

import java.util.HashMap;
import java.util.Map;

public interface Converter {

    Component of(Long resumeId);

    static Map<String, String> from(Component component) {
        Map<String, String> result = new HashMap<>();

        result.put(component.getProperty(), component.getContent());
        if (component.getStartDate() != null) {
            result.put(component.getProperty() + "StartDate", component.getStartDate().toString());
        }
        if (component.getEndDate() != null) {
            result.put(component.getProperty() + "EndDate", component.getEndDate().toString());
        }

        if (component.getComponents() != null) {
            for (Component subComponent : component.getComponents()) {
                result.putAll(from(subComponent));
            }
        }

        return result;
    }

}
