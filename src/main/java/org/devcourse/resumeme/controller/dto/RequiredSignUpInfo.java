package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.user.Role;

public record RequiredSignUpInfo(Long id, String nickname, String realName, String phoneNumber, Role role) {

    public RequiredSignUpInfo(Long id, String nickname, String realName, String phoneNumber, Role role) {
        this.id = id;
        this.nickname = nickname;
        this.realName = realName;
        this.phoneNumber = phoneNumber;
        role.checkSignUpRequest();
        this.role = role;
    }

}
