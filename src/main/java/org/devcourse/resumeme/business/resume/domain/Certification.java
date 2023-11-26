package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.devcourse.resumeme.business.resume.domain.Property.AUTHORITY;
import static org.devcourse.resumeme.business.resume.domain.Property.DESCRIPTION;
import static org.devcourse.resumeme.business.resume.domain.Property.LINK;
import static org.devcourse.resumeme.business.resume.domain.Property.TITLE;
import static org.devcourse.resumeme.common.util.Validator.notNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Certification implements Converter {

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

    public Certification(Map<Property, Component> components) {
        this.certificationTitle = components.get(TITLE).getContent();
        this.acquisitionDate = components.get(TITLE).getStartDate().toString();
        this.issuingAuthority = components.get(AUTHORITY).getContent();
        this.link = components.get(LINK).getContent();
        this.description = components.get(DESCRIPTION).getContent();
    }

    @Override
    public Component of(Long resumeId) {
        Component authority = new Component(AUTHORITY, issuingAuthority, resumeId);
        Component link = new Component(LINK, this.link, resumeId);
        Component description = new Component(DESCRIPTION, this.description, resumeId);

        return new Component(TITLE, certificationTitle, LocalDate.parse(acquisitionDate), null, resumeId, List.of(authority, link, description));
    }

}
