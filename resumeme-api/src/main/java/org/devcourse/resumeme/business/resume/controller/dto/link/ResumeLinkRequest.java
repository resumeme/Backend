package org.devcourse.resumeme.business.resume.controller.dto.link;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.ComponentCreateRequest;
import org.devcourse.resumeme.business.resume.domain.link.LinkType;
import org.devcourse.resumeme.business.resume.domain.link.ReferenceLink;
import org.devcourse.resumeme.business.resume.service.vo.ReferenceLinkDomainVo;

@Getter
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
