package org.devcourse.resumeme.domain.mentee;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.domain.user.Role;

import static org.devcourse.resumeme.common.util.Validator.validate;
import static org.devcourse.resumeme.domain.user.Role.ROLE_MENTEE;
import static org.devcourse.resumeme.domain.user.Role.ROLE_PENDING;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequiredInfo {

    private static final String PHONE_REGEX = "^01(0|1|[6-9])[0-9]{3,4}[0-9]{4}$";

    private String nickname;

    private String realName;

    private String phoneNumber;

    @Getter
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public RequiredInfo(String realName, String nickname, String phoneNumber, Role role) {
        validateRequiredInfo(nickname, realName, phoneNumber, role);
        this.realName = realName;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    private void validateRequiredInfo(String nickname, String realName, String phoneNumber, Role role) {
        validate(nickname == null || nickname.isBlank(), "INVALID_TEXT", "닉네임이 유효하지 않습니다." );
        validate(realName == null || nickname.isBlank(), "INVALID_TEXT", "이름이 유효하지 않습니다.");
        validate(phoneNumber == null || !(phoneNumber.matches(PHONE_REGEX)), "INVALID_TEXT", "전화번호가 유효하지 않습니다.");
        validate(!role.equals(ROLE_MENTEE) && !role.equals(ROLE_PENDING),"INVALID_ROLE", "선택 불가능한 역할입니다");
    }

}
