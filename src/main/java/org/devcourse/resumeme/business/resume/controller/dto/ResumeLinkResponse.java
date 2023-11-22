package org.devcourse.resumeme.business.resume.controller.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.domain.Converter;
import org.devcourse.resumeme.business.resume.domain.ReferenceLink;
import org.devcourse.resumeme.business.resume.entity.Component;

@Data
@NoArgsConstructor
@JsonTypeName("links")
public class ResumeLinkResponse extends ComponentResponse {

    private String linkType;
    private String url;

    public ResumeLinkResponse(String type, Component component) {
        super(type, component);
        ReferenceLink referenceLink = new ReferenceLink(Converter.from(component));
        this.linkType = referenceLink.getLinkType().name();
        this.url = referenceLink.getAddress();
    }

}
