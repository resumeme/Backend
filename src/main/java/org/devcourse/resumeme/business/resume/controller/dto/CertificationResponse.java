package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.Certification;
import org.devcourse.resumeme.business.resume.entity.Component;

@Data
@NoArgsConstructor
public class CertificationResponse extends ComponentResponse {

    private String certificationTitle;

    private String acquisitionDate;

    private String issuingAuthority;

    private String link;

    private String description;

    public CertificationResponse(Certification certification, Component component) {
        super(component);
        this.certificationTitle = certification.getCertificationTitle();
        this.acquisitionDate = certification.getAcquisitionDate();
        this.issuingAuthority = certification.getIssuingAuthority();
        this.link = certification.getLink();
        this.description = certification.getDescription();
    }

}
