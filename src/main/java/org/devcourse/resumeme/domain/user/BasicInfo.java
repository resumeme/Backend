package org.devcourse.resumeme.domain.user;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class BasicInfo {

    private String name;

    private String nickname;

    private String phoneNumber;

    private String email;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String socialId;

    private String refreshToken;

}
