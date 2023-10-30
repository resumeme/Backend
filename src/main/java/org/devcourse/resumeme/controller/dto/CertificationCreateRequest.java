package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.resume.Certification;
import org.devcourse.resumeme.domain.resume.Resume;

public record CertificationCreateRequest(
        String certificationTitle,
        String acquisitionDate,
        String issuingAuthority,
        String link,
        String description
) {
    public Certification toEntity(Resume resume) {
        return new Certification(certificationTitle, acquisitionDate, issuingAuthority, link, description);
    }
}
