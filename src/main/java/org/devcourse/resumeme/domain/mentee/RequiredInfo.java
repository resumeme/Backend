package org.devcourse.resumeme.domain.mentee;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.domain.user.Role;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

import static org.devcourse.resumeme.common.util.Validator.validate;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequiredInfo {

    private static final String PHONE_REGEX = "^01(0|1|[6-9])[0-9]{3,4}[0-9]{4}$";

    @Getter
    private String nickname;

    @Getter
    private String realName;

    @Getter
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
        validateNickname(nickname);
        validateRealName(realName);
        validatePhoneNumber(phoneNumber);
        validateUserRole(role);
    }

    public void updateNickname(String nickname) {
        validateNickname(nickname);
        this.nickname = nickname;
    }

    public void updatePhoneNumber(String phoneNumber) {
        validatePhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    public void updateRole(Role role) {
        validateUserRole(role);
        this.role = role;
    }

    private void validateNickname(String nickname) {
        validate(nickname == null || nickname.isBlank(), "INVALID_TEXT", "닉네임이 유효하지 않습니다.");
    }

    private void validateRealName(String realName) {
        validate(realName == null || realName.isBlank(), "INVALID_TEXT", "이름이 유효하지 않습니다.");
    }

    private void validatePhoneNumber(String phoneNumber) {
        validate(phoneNumber == null || !(phoneNumber.matches(PHONE_REGEX)), "INVALID_TEXT", "전화번호가 유효하지 않습니다.");
    }

    private void validateUserRole(Role role) {
        validate(Role.ROLE_ADMIN.equals(role), ExceptionCode.ROLE_NOT_ALLOWED);
    }

}
