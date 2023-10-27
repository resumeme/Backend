package org.devcourse.resumeme.controller.dto;

import lombok.Getter;
import org.devcourse.resumeme.domain.user.Role;

public record RegisterInfoRequest(String cacheKey, RequiredInfoRequest requiredInfoRequest) {

    @Getter
    public record RequiredInfoRequest(String nickname, String realName, String phoneNumber,Role role) {

    }
}
