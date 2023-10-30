package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.user.Role;

public record RequiredInfoRequest(String nickname, String realName, String phoneNumber, Role role) {

}
