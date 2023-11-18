package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReferenceLink implements Converter {

    private LinkType linkType;

    private String address;

    public ReferenceLink(LinkType linkType, String address) {
        this.linkType = linkType;
        this.address = address;
    }

    public ReferenceLink(Map<String, String> component) {
        this(LinkType.valueOf(component.get("type")), component.get("address"));
    }

    @Override
    public Component of(Long resumeId) {
        Component address = new Component("address", this.address, null, null, resumeId, null);

        return new Component("type", linkType.name(), null, null, resumeId, List.of(address));
    }

}
