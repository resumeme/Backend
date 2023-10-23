package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.user.Role;

public record PrimarySignUpInfo(Long id, String realName, String phoneNumber, Role role) {

    public PrimarySignUpInfo(Long id, String realName, String phoneNumber, Role role) {
        this.id = id;
        this.realName = realName;
        this.phoneNumber = phoneNumber;
        role.checkSignUpRequest();
        this.role = role;
    }

}
