package org.devcourse.resumeme.business.user.domain.mentee;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.global.exception.ExceptionCode;

import static org.devcourse.resumeme.common.util.Validator.check;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequiredInfo {

    private static final String REALNAME_REGEX = "^[ㄱ-ㅎ|가-힣]*$";

    private static final String PHONE_REGEX = "^01(0|1|[6-9])[0-9]{3,4}[0-9]{4}$";

    @Getter
    private String nickname;

    @Getter
    private String realName;

    @Getter
    private String phoneNumber;

    @Getter
    private Role role;

    @Builder
    public RequiredInfo(String realName, String nickname, String phoneNumber, Role role) {
        validateRequiredInfo(nickname, realName, phoneNumber, role);
        this.realName = realName;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public static RequiredInfo of(User user) {
        return RequiredInfo.builder()
                .realName(user.getRequiredInfo().getRealName())
                .nickname(user.getRequiredInfo().getNickname())
                .phoneNumber(user.getRequiredInfo().getPhoneNumber())
                .role(user.getRequiredInfo().getRole())
                .build();
    }

    private void validateRequiredInfo(String nickname, String realName, String phoneNumber, Role role) {
        validateNickname(nickname);
        validateRealName(realName);
        validatePhoneNumber(phoneNumber);
        validateUserRole(role);
    }

    public void updateRole(Role role) {
        validateUserRole(role);
        this.role = role;
    }

    public void update(String nickname, String phoneNumber) {
        validateNickname(nickname);
        validatePhoneNumber(phoneNumber);
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

    private void validateNickname(String nickname) {
        check(nickname == null || nickname.isBlank(), "INVALID_TEXT", "닉네임이 유효하지 않습니다");
    }

    private void validateRealName(String realName) {
        check(realName == null || realName.isBlank(), "INVALID_TEXT", "이름이 유효하지 않습니다.");
        check(!(realName.matches(REALNAME_REGEX)) || realName.length() > 6, "INVALID_TEXT", "이름이 유효하지 않습니다");
    }

    private void validatePhoneNumber(String phoneNumber) {
        check(phoneNumber == null || !(phoneNumber.matches(PHONE_REGEX)), "INVALID_TEXT", "전화번호가 유효하지 않습니다");
    }

    private void validateUserRole(Role role) {
        Validator.check(Role.ROLE_ADMIN.equals(role), ExceptionCode.ROLE_NOT_ALLOWED);
    }

}
