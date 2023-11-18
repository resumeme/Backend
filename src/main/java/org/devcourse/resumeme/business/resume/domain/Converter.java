package org.devcourse.resumeme.business.resume.domain;

import org.devcourse.resumeme.business.resume.entity.Component;

import java.util.HashMap;
import java.util.Map;

import static org.devcourse.resumeme.business.resume.domain.Property.END_DATE;
import static org.devcourse.resumeme.business.resume.domain.Property.START_DATE;

public interface Converter {

    Component of(Long resumeId);

    static Map<String, String> from(Component component) {
        Map<String, String> result = new HashMap<>();

        result.put(component.getProperty(), component.getContent());
        if (component.getStartDate() != null) {
            result.put(component.getProperty() + START_DATE, component.getStartDate().toString());
        }
        if (component.getEndDate() != null) {
            result.put(component.getProperty() + END_DATE, component.getEndDate().toString());
        }

        if (component.getComponents() != null) {
            for (Component subComponent : component.getComponents()) {
                result.putAll(from(subComponent));
            }
        }

        return result;
    }

}
