package org.devcourse.resumeme.domain.user;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
public class RequiredInfo {

    private String nickname;

    private String realName;

    private String phoneNumber;

    @Builder
    public RequiredInfo(String nickname, String realName, String phoneNumber) {
        this.nickname = nickname;
        this.realName = realName;
        this.phoneNumber = phoneNumber;
    }

}
