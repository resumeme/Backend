package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.devcourse.resumeme.business.resume.domain.Property.TYPE;
import static org.devcourse.resumeme.business.resume.domain.Property.URL;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReferenceLink {

    private LinkType linkType;

    private String address;

    private ComponentInfo componentInfo;

    public ReferenceLink(LinkType linkType, String address) {
        this.linkType = linkType;
        this.address = address;
    }

    @Builder
    private ReferenceLink(LinkType linkType, String address, Component component) {
        this.linkType = linkType;
        this.address = address;
        this.componentInfo = new ComponentInfo(component);
    }

    public Component toComponent(Long resumeId) {
        Component address = new Component(URL, this.address, resumeId);

        return new Component(TYPE, linkType.name(), null, null, resumeId, List.of(address));
    }

    public static ReferenceLink of(List<Component> components) {
        ReferenceLinkConverter converter = ReferenceLinkConverter.of(components);

        return ReferenceLink.of(converter);
    }

    public static List<ReferenceLink> of(Component component) {
        if (component == null) {
            return new ArrayList<>();
        }

        return component.getComponents().stream()
                .map(ReferenceLinkConverter::of)
                .map(ReferenceLink::of)
                .toList();
    }

    private static ReferenceLink of(ReferenceLinkConverter converter) {
        return ReferenceLink.builder()
                .component(converter.type)
                .linkType(LinkType.valueOf(converter.type.getContent()))
                .address(converter.details.address.getContent())
                .build();
    }

    @Builder
    private static class ReferenceLinkConverter {

        private Component type;

        private ReferenceLinkDetails details;

        @Builder
        private static class ReferenceLinkDetails {

            private Component address;

            public static ReferenceLinkDetails of(Component component) {
                Map<Property, Component> componentMap = component.getComponents().stream()
                        .collect(toMap(Component::getProperty, identity()));

                return ReferenceLinkDetails.builder()
                        .address(componentMap.get(URL))
                        .build();
            }

        }

        private static ReferenceLinkConverter of(List<Component> components) {
            Map<Property, Component> componentMap = components.stream()
                    .collect(toMap(Component::getProperty, identity()));

            ReferenceLinkDetails details = ReferenceLinkDetails.builder()
                    .address(componentMap.get(URL))
                    .build();

            return ReferenceLinkConverter.builder()
                    .type(componentMap.get(TYPE))
                    .details(details)
                    .build();
        }

        private static ReferenceLinkConverter of(Component component) {
            return ReferenceLinkConverter.builder()
                    .type(component)
                    .details(ReferenceLinkDetails.of(component))
                    .build();
        }

    }

}
