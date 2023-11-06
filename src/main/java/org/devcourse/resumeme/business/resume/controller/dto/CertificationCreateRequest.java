package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.Certification;
import org.devcourse.resumeme.business.resume.domain.Resume;

public record CertificationCreateRequest(
        String certificationTitle,
        String acquisitionDate,
        String issuingAuthority,
        String link,
        String description
) {
    public Certification toEntity(Resume resume) {
        return new Certification(resume, certificationTitle, acquisitionDate, issuingAuthority, link, description);
    }
}
