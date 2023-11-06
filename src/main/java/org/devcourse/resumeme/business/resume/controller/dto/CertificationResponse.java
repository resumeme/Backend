package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.Certification;

public record CertificationResponse(
        String certificationTitle,
        String acquisitionDate,
        String issuingAuthority,
        String link,
        String description
) {

    public CertificationResponse(Certification certification) {
        this(
                certification.getCertificationTitle(),
                certification.getAcquisitionDate(),
                certification.getIssuingAuthority(),
                certification.getLink(),
                certification.getDescription()
        );
    }
}
