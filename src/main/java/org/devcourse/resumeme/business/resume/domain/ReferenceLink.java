package org.devcourse.resumeme.business.resume.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReferenceLink {

    @Enumerated(STRING)
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
