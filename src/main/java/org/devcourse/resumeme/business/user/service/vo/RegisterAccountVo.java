package org.devcourse.resumeme.business.user.service.vo;

import org.devcourse.resumeme.global.auth.model.jwt.Claims;

public record RegisterAccountVo(String cacheKey, Claims claims) {

}
