package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.util.List;
import java.util.Map;

import static org.devcourse.resumeme.business.resume.domain.Property.TYPE;
import static org.devcourse.resumeme.business.resume.domain.Property.URL;

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
        this(LinkType.valueOf(component.get(TYPE.name())), component.get(URL.name()));
    }

    @Override
    public Component of(Long resumeId) {
        Component address = new Component(URL, this.address, resumeId);

        return new Component(TYPE, linkType.name(), null, null, resumeId, List.of(address));
    }

}
