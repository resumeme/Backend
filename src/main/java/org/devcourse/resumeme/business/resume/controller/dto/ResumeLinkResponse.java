package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.ReferenceLink;

@Data
public class ResumeLinkResponse extends ComponentResponse {

    private String linkType;
    private String url;

    public ResumeLinkResponse(ReferenceLink referenceLink, Long id) {
        super(id);
        this.linkType = referenceLink.getLinkType().name();
        this.url = referenceLink.getAddress();
    }

}
