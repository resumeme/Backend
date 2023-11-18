package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    public Certification(Map<String, String> component) {
        this(component.get("title"), component.get("titleStartDate"), component.get("authority"), component.get("link"), component.get("description"));
    }

    @Override
    public Component of(Long resumeId) {
        Component authority = new Component("authority", issuingAuthority, null, null, resumeId, null);
        Component link = new Component("link", this.link, null, null, resumeId, null);
        Component description = new Component("description", this.description, null, null, resumeId, null);

        return new Component("title", certificationTitle, LocalDate.parse(acquisitionDate), null, resumeId, List.of(authority, link, description));
    }

}
