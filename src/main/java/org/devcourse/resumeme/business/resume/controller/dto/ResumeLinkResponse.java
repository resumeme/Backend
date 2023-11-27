package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Getter;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.Converter;
import org.devcourse.resumeme.business.resume.domain.ReferenceLink;

@Getter
public class ResumeLinkResponse extends ComponentResponse {

    private String linkType;

    private String url;

    public ResumeLinkResponse(Converter converter) {
        super(converter);
        ReferenceLink referenceLink = (ReferenceLink) converter;
        this.linkType = referenceLink.getLinkType().name();
        this.url = referenceLink.getAddress();
    }

}
