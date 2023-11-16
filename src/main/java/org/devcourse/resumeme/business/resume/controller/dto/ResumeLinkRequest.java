package org.devcourse.resumeme.business.resume.controller.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.devcourse.resumeme.business.resume.domain.LinkType;
import org.devcourse.resumeme.business.resume.domain.ReferenceLink;

@Getter @Setter
@NoArgsConstructor
@JsonTypeName("links")
public class ResumeLinkRequest extends ComponentCreateRequest {

    private String linkType;

    private String url;

    public ResumeLinkRequest(String linkType, String url) {
        this.linkType = linkType;
        this.url = url;
    }

    public ReferenceLink toEntity() {
        return new ReferenceLink(LinkType.valueOf(linkType), url);
    }

}
