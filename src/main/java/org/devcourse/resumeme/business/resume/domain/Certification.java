package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;
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
public class Certification extends Converter {

    private String certificationTitle;

    private String acquisitionDate;

    private String issuingAuthority;

    private String link;

    private String description;

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
        super(component);
        notNull(certificationTitle);

        this.certificationTitle = certificationTitle;
        this.acquisitionDate = acquisitionDate;
        this.issuingAuthority = issuingAuthority;
        this.link = link;
        this.description = description;
    }

    @Override
    public Component toComponent(Long resumeId) {
        Component authority = new Component(AUTHORITY, issuingAuthority, resumeId);
        Component link = new Component(LINK, this.link, resumeId);
        Component description = new Component(DESCRIPTION, this.description, resumeId);

        return new Component(TITLE, certificationTitle, LocalDate.parse(acquisitionDate), null, resumeId, List.of(authority, link, description));
    }

    public static Certification of(List<Component> components) {
        CertificationConverter converter = CertificationConverter.of(components);

        return Certification.of(converter);
    }

    private static Certification of(CertificationConverter converter) {
        return Certification.builder()
                .component(converter.certification)
                .certificationTitle(converter.certification.getContent())
                .acquisitionDate(converter.certification.getStartDate().toString())
                .issuingAuthority(converter.details.getAuthority().getContent())
                .link(converter.details.getLink().getContent())
                .description(converter.details.getDescription().getContent())
                .build();
    }

    @Data
    @Builder
    private static class CertificationConverter {

        private Component certification;

        private CertificationDetails details;

        @Data
        @Builder
        private static class CertificationDetails {

            private Component authority;

            private Component link;

            private Component description;

        }

        private static CertificationConverter of(List<Component> components) {
            Map<Property, Component> componentMap = components.stream()
                    .collect(toMap(Component::getProperty, identity()));

            CertificationDetails details = CertificationDetails.builder()
                    .authority(componentMap.get(AUTHORITY))
                    .link(componentMap.get(LINK))
                    .description(componentMap.get(DESCRIPTION))
                    .build();

            return CertificationConverter.builder()
                    .certification(componentMap.get(TITLE))
                    .details(details)
                    .build();
        }

    }

}
