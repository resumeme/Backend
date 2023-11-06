package org.devcourse.resumeme.business.resume.controller.dto;

import org.devcourse.resumeme.business.resume.domain.LinkType;
import org.devcourse.resumeme.business.resume.domain.ReferenceLink;

public record ResumeLinkRequest(
        String linkType,
        String url
) {

    public ReferenceLink toEntity() {
        return new ReferenceLink(LinkType.valueOf(linkType), url);
    }

}
