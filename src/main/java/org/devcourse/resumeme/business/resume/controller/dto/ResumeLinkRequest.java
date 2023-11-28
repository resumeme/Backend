package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.devcourse.resumeme.business.resume.domain.LinkType;
import org.devcourse.resumeme.business.resume.domain.ReferenceLink;
import org.devcourse.resumeme.business.resume.service.vo.ReferenceLinkDomainVo;

@Getter @Setter
@NoArgsConstructor
public class ResumeLinkRequest extends ComponentCreateRequest {

    private String linkType;

    private String url;

    public ResumeLinkRequest(String linkType, String url) {
        this.linkType = linkType;
        this.url = url;
    }

    public ReferenceLinkDomainVo toVo() {
        ReferenceLink referenceLink = new ReferenceLink(LinkType.valueOf(linkType), url);

        return new ReferenceLinkDomainVo(referenceLink);
    }

}
