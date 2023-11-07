package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.ReferenceLink;

public record ResumeLinkResponse(
        String linkType,
        String url
) {

    public ResumeLinkResponse(ReferenceLink referenceLink) {
        this(
                referenceLink.getLinkType(),
                referenceLink.getAddress()
        );
    }

}
