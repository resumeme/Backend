package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.Certification;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CertificationResponse extends ComponentResponse {

    private String certificationTitle;

    private String acquisitionDate;

    private String issuingAuthority;

    private String link;

    private String description;

    public CertificationResponse(
            Long id, LocalDateTime createdDate, String certificationTitle,
            String acquisitionDate,
            String issuingAuthority,
            String link,
            String description
    ) {
        super(id, createdDate);
        this.certificationTitle = certificationTitle;
        this.acquisitionDate = acquisitionDate;
        this.issuingAuthority = issuingAuthority;
        this.link = link;
        this.description = description;
    }

    public CertificationResponse(Certification certification, Long id, LocalDateTime createdDate) {
        this(
                id, createdDate,
                certification.getCertificationTitle(),
                certification.getAcquisitionDate(),
                certification.getIssuingAuthority(),
                certification.getLink(),
                certification.getDescription()
        );
    }

}
