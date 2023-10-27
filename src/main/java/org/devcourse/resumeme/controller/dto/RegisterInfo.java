package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.mentee.RequiredInfo;

public record RegisterInfo(String cacheKey, RequiredInfo requiredInfo) {

}
