package org.devcourse.resumeme.business.user.service.vo;

import org.devcourse.resumeme.business.user.entity.User;

public record UserResponse(Long userId, String nickname, String name, String email, String phoneNumber, String imageUrl) {

    public UserResponse(User user) {
        this(user.getId(), user.getRequiredInfo().getNickname(), user.getRequiredInfo().getRealName(), user.getEmail(), user.getRequiredInfo().getPhoneNumber(), user.getImageUrl());
    }

}
