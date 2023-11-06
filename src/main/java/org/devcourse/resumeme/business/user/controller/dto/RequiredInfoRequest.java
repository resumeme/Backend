package org.devcourse.resumeme.business.user.controller.dto;

import org.devcourse.resumeme.business.user.domain.Role;

public record RequiredInfoRequest(String nickname, String realName, String phoneNumber, Role role) {

}
