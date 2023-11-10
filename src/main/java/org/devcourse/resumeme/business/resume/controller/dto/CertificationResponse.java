package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.Certification;

@Data
public class CertificationResponse implements ComponentResponse {

    private String certificationTitle;

    private String acquisitionDate;

    private String issuingAuthority;

    private String link;

    private String description;

    public CertificationResponse(
            String certificationTitle,
            String acquisitionDate,
            String issuingAuthority,
            String link,
            String description
    ) {
        this.certificationTitle = certificationTitle;
        this.acquisitionDate = acquisitionDate;
        this.issuingAuthority = issuingAuthority;
        this.link = link;
        this.description = description;
    }

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
