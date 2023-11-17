package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.ReferenceLink;

import java.time.LocalDateTime;

@Data
public class ResumeLinkResponse extends ComponentResponse {

    private String linkType;
    private String url;

    public ResumeLinkResponse(ReferenceLink referenceLink, Long id, LocalDateTime createdDate) {
        super(id, createdDate);
        this.linkType = referenceLink.getLinkType().name();
        this.url = referenceLink.getAddress();
    }

}
