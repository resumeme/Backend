package org.devcourse.resumeme.business.resume.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReferenceLink {

    private LinkType linkType;

    @Getter
    private String address;

    public ReferenceLink(LinkType linkType, String address) {
        this.linkType = linkType;
        this.address = address;
    }

    public String getLinkType() {
        return linkType.toString();
    }

}
