package org.devcourse.resumeme.business.resume.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.career.Career;
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

    protected Converter(Component component) {
        this.componentId = component.getId();
        this.originComponentId = component.getOriginComponentId();
        this.reflectFeedback = component.isReflectFeedBack();
    }

    public abstract Component toComponent(Long resumeId);

    public static List<Converter> of(Component component) {
        return component.getComponents().stream()
                .map(subComponent -> getConverters(component, flatComponents(subComponent)))
                .toList();
    }

    private static Converter getConverters(Component component, List<Component> components) {
        return switch (component.getProperty()) {
            case ACTIVITIES -> Activity.of(components);
            case CAREERS -> Career.of(components);
            case CERTIFICATIONS -> Certification.of(components);
            case FOREIGNLANGUAGES -> ForeignLanguage.of(components);
            case PROJECTS -> Project.of(components);
            case TRAININGS -> Training.of(components);
            case LINKS -> ReferenceLink.of(components);
            default -> throw new IllegalStateException("Unexpected value: " + component.getProperty());
        };
    }

    private static List<Component> flatComponents(Component component) {
        if (component.isType(DUTY)) {
            return List.of(component);
        }

        List<Component> result = new ArrayList<>();
        result.add(component);
        if (component.getComponents() != null) {
            for (Component subComponent : component.getComponents()) {
                result.addAll(flatComponents(subComponent));
            }
        }

        return result;
    }


}
