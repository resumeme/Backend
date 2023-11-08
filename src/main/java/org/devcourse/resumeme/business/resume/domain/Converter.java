package org.devcourse.resumeme.business.resume.domain;

import org.devcourse.resumeme.business.resume.entity.Component;

public interface Converter {

    Component of(Long resumeId);

}
