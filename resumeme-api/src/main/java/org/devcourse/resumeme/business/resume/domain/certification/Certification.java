package org.devcourse.resumeme.business.resume.domain.certification;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.domain.ComponentInfo;
import org.devcourse.resumeme.business.resume.domain.Property;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.devcourse.resumeme.business.resume.domain.Property.AUTHORITY;
import static org.devcourse.resumeme.business.resume.domain.Property.DESCRIPTION;
import static org.devcourse.resumeme.business.resume.domain.Property.LINK;
import static org.devcourse.resumeme.business.resume.domain.Property.TITLE;
import static org.devcourse.resumeme.common.util.Validator.notNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Certification {

    private String certificationTitle;

    private String acquisitionDate;

    private String issuingAuthority;

    private String link;

    private String description;

    private ComponentInfo componentInfo;

    public Certification(String certificationTitle, String acquisitionDate, String issuingAuthority, String link, String description) {
        notNull(certificationTitle);

        this.certificationTitle = certificationTitle;
        this.acquisitionDate = acquisitionDate;
        this.issuingAuthority = issuingAuthority;
        this.link = link;
        this.description = description;
    }

    @Builder
    private Certification(String certificationTitle, String acquisitionDate, String issuingAuthority, String link, String description, Component component) {
        notNull(certificationTitle);

        this.certificationTitle = certificationTitle;
        this.acquisitionDate = acquisitionDate;
        this.issuingAuthority = issuingAuthority;
        this.link = link;
        this.description = description;
        this.componentInfo = new ComponentInfo(component);
    }

    public Component toComponent(Long resumeId) {
        Component authority = new Component(AUTHORITY, issuingAuthority, resumeId);
        Component link = new Component(LINK, this.link, resumeId);
        Component description = new Component(DESCRIPTION, this.description, resumeId);

        return new Component(TITLE, certificationTitle, LocalDate.parse(acquisitionDate), null, resumeId, List.of(authority, link, description));
    }

    public static List<Certification> of(Component component) {
        if (component == null) {
            return new ArrayList<>();
        }

        return component.getComponents().stream()
                .map(CertificationConverter::of)
                .map(Certification::of)
                .toList();
    }

    private static Certification of(CertificationConverter converter) {
        return Certification.builder()
                .component(converter.certification)
                .certificationTitle(converter.certification.getContent())
                .acquisitionDate(converter.certification.getStartDate().toString())
                .issuingAuthority(converter.details.authority.getContent())
                .link(converter.details.link.getContent())
                .description(converter.details.description.getContent())
                .build();
    }

    @Builder
    private static class CertificationConverter {

        private Component certification;

        private CertificationDetails details;

        @Builder
        private static class CertificationDetails {

            private Component authority;

            private Component link;

            private Component description;

            public static CertificationDetails of(Component component) {
                Map<Property, Component> componentMap = component.getComponents().stream()
                        .collect(toMap(Component::getProperty, identity()));

                return CertificationDetails.builder()
                        .authority(componentMap.get(AUTHORITY))
                        .link(componentMap.get(LINK))
                        .description(componentMap.get(DESCRIPTION))
                        .build();
            }

        }

        private static CertificationConverter of(Component component) {
            return CertificationConverter.builder()
                    .certification(component)
                    .details(CertificationDetails.of(component))
                    .build();
        }

    }

}
