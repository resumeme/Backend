package org.devcourse.resumeme.business.resume.controller.dto.certification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentCreateRequest;
import org.devcourse.resumeme.business.resume.domain.certification.Certification;
import org.devcourse.resumeme.business.resume.service.vo.CertificationDomainVo;

@Getter @Setter
@NoArgsConstructor
public class CertificationCreateRequest extends ComponentCreateRequest {

    private String certificationTitle;

    private String acquisitionDate;

    private String issuingAuthority;

    private String link;

    private String description;

    public CertificationCreateRequest(
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

    @Override
    public CertificationDomainVo toVo() {
        Certification certification = new Certification(certificationTitle, acquisitionDate, issuingAuthority, link, description);

        return new CertificationDomainVo(certification);
    }

}
