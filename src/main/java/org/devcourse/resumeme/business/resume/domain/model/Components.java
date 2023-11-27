package org.devcourse.resumeme.business.resume.domain.model;

import org.devcourse.resumeme.business.resume.domain.Property;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.Double.parseDouble;

public record Components(Map<Property, Component> components) {

    public Component getComponent(Property property) {
        return components.get(property);
    }

    public String getContent(Property property) {
        Component component = components.get(property);

        if (component == null) {
            return "";
        }

        return component.getContent();
    }

    public double toDouble(Property property) {
        Component component = components.get(property);

        if (component == null) {
            return 0;
        }

        return parseDouble(component.getContent());

    }

    public List<String> toList(Property property) {
        return Arrays.asList(getContent(property).split(","));
    }

    public LocalDate getStartDate(Property property) {
        return components.get(property).getStartDate();
    }

    public LocalDate getEndDate(Property property) {
        return components.get(property).getEndDate();
    }

}
