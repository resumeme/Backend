package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.ReferenceLink;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class ResumeLinkResponse extends ComponentResponse {

    private String linkType;

    private String url;

    public ResumeLinkResponse(ReferenceLink referenceLink) {
        super(referenceLink.getComponentInfo());
        this.linkType = referenceLink.getLinkType().name();
        this.url = referenceLink.getAddress();
    }

}
