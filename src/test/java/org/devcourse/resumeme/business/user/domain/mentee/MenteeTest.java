package org.devcourse.resumeme.business.user.domain.mentee;

import org.devcourse.resumeme.business.user.controller.dto.RequiredInfoRequest;
import org.devcourse.resumeme.business.user.controller.dto.mentee.MenteeRegisterInfoRequest;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;
import org.devcourse.resumeme.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenteeTest {

    private RequiredInfoRequest requiredInfoRequest;

    private MenteeRegisterInfoRequest menteeRegisterInfoRequest;

    private OAuth2TempInfo oAuth2TempInfo;

    private String refreshToken;

    @BeforeEach
    void SetUP() {
        requiredInfoRequest = new RequiredInfoRequest("nickname", "김한주", "01034548443", "mentee");
        menteeRegisterInfoRequest = new MenteeRegisterInfoRequest("cacheKey", requiredInfoRequest, Set.of("FRONT", "BACK"), Set.of("COMMERCE", "FINANCE"), "안녕하세요 취업하고싶어요.");
        oAuth2TempInfo = new OAuth2TempInfo(null, "GOOGLE", "지롱", "devcoco@naver.com", "image.png");
        refreshToken = "refreshTokenRecentlyIssued";
    }

    @Test
    void 멘티_생성에_성공한다() {
        Mentee.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(refreshToken)
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), Role.of(requiredInfoRequest.role())))
                .interestedPositions(menteeRegisterInfoRequest.interestedPositions())
                .interestedFields(menteeRegisterInfoRequest.interestedFields())
                .introduce(menteeRegisterInfoRequest.introduce())
                .build();
    }

    @Test
    void 필수정보_미입력시_멘티_생성에_실패한다() {
        assertThatThrownBy(() -> Mentee.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(refreshToken)
                .requiredInfo(null)
                .interestedPositions(menteeRegisterInfoRequest.interestedPositions())
                .interestedFields(menteeRegisterInfoRequest.interestedFields())
                .introduce(menteeRegisterInfoRequest.introduce())
                .build()).isInstanceOf(CustomException.class)
                .hasMessage("빈 값일 수 없습니다");
    }

    @Test
    void 관심직무_관심도메인은_선택하지_않아도_멘티_생성에_성공한다() {
        Set<String> emptyInterestedPositions = Set.of();
        Set<String> emptyInterestedFields = Set.of();

        Mentee.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(refreshToken)
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), Role.of(requiredInfoRequest.role())))
                .interestedPositions(emptyInterestedPositions)
                .interestedFields(emptyInterestedFields)
                .introduce(menteeRegisterInfoRequest.introduce())
                .build();
    }

    @Test
    void 유효하지_않은_이메일_입력시_멘티_생성에_실패한다() {
        String invalidEmail = "dsklk3ssssssf@";

        assertThatThrownBy(() -> Mentee.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(invalidEmail)
                .refreshToken(refreshToken)
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), Role.of(requiredInfoRequest.role())))
                .interestedPositions(menteeRegisterInfoRequest.interestedPositions())
                .interestedFields(menteeRegisterInfoRequest.interestedFields())
                .introduce(menteeRegisterInfoRequest.introduce())
                .build()).isInstanceOf(CustomException.class)
                .hasMessage("이메일이 유효하지 않습니다");
    }

    @Test
    void 멘토_또는_관리자_역할로는_멘토_생성에_실패한다() {
        Role pending = Role.ROLE_PENDING;
        Role mentor = Role.ROLE_MENTOR;
        Role admin = Role.ROLE_ADMIN;

        assertThatThrownBy(() -> Mentee.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(refreshToken)
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), pending))
                .interestedPositions(menteeRegisterInfoRequest.interestedPositions())
                .interestedFields(menteeRegisterInfoRequest.interestedFields())
                .introduce(menteeRegisterInfoRequest.introduce())
                .build()).isInstanceOf(CustomException.class)
                .hasMessage("허용되지 않은 역할입니다");

        assertThatThrownBy(() -> Mentee.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(refreshToken)
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), mentor))
                .interestedPositions(menteeRegisterInfoRequest.interestedPositions())
                .interestedFields(menteeRegisterInfoRequest.interestedFields())
                .introduce(menteeRegisterInfoRequest.introduce())
                .build()).isInstanceOf(CustomException.class)
                .hasMessage("허용되지 않은 역할입니다");

        assertThatThrownBy(() -> Mentee.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(refreshToken)
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), admin))
                .interestedPositions(menteeRegisterInfoRequest.interestedPositions())
                .interestedFields(menteeRegisterInfoRequest.interestedFields())
                .introduce(menteeRegisterInfoRequest.introduce())
                .build()).isInstanceOf(CustomException.class)
                .hasMessage("허용되지 않은 역할입니다");
    }

}
