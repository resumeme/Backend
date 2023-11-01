package org.devcourse.resumeme.domain.mentor;

import org.devcourse.resumeme.controller.dto.MentorRegisterInfoRequest;
import org.devcourse.resumeme.controller.dto.RequiredInfoRequest;
import org.devcourse.resumeme.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.domain.user.Provider;
import org.devcourse.resumeme.domain.user.Role;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.auth.model.OAuth2TempInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MentorTest {

    private RequiredInfoRequest requiredInfoRequest;

    private MentorRegisterInfoRequest mentorRegisterInfoRequest;

    private OAuth2TempInfo oAuth2TempInfo;

    private String refreshToken;

    @BeforeEach
    void SetUP() {
        requiredInfoRequest = new RequiredInfoRequest("nickname", "realName", "01034548443", Role.ROLE_PENDING);
        mentorRegisterInfoRequest = new MentorRegisterInfoRequest("cacheKey", requiredInfoRequest, Set.of("FRONT", "BACK"), "A회사 00팀, B회사 xx팀", 3, "안녕하세요 멘토가 되고싶어요.");
        oAuth2TempInfo = new OAuth2TempInfo( null, "GOOGLE", "지롱", "devcoco@naver.com", "image.png");
        refreshToken = "refreshTokenRecentlyIssued";
    }

    @Test
    void 멘토_생성에_성공한다() {
        Mentor.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(refreshToken)
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), requiredInfoRequest.role()))
                .experiencedPositions(mentorRegisterInfoRequest.experiencedPositions())
                .careerContent(mentorRegisterInfoRequest.careerContent())
                .careerYear(mentorRegisterInfoRequest.careerYear())
                .build();
    }

    @Test
    void 필수정보_미입력시_멘토_생성에_실패한다() {

        assertThatThrownBy(() -> Mentor.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(refreshToken)
                .requiredInfo(null)
                .experiencedPositions(mentorRegisterInfoRequest.experiencedPositions())
                .careerContent(mentorRegisterInfoRequest.careerContent())
                .careerYear(mentorRegisterInfoRequest.careerYear())
                .build()).isInstanceOf(CustomException.class)
                .hasMessage("빈 값일 수 없습니다");
    }

    @Test
    void 활동직무_미입력시_멘토_생성에_실패한다() {
        Set<String> emptyExperiencedPositions = Set.of();

        assertThatThrownBy(() -> Mentor.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(refreshToken)
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(),  requiredInfoRequest.role()))
                .experiencedPositions(emptyExperiencedPositions)
                .careerContent(mentorRegisterInfoRequest.careerContent())
                .careerYear(mentorRegisterInfoRequest.careerYear())
                .build()).isInstanceOf(CustomException.class)
                .hasMessage("빈 값일 수 없습니다");
    }

    @Test
    void 경력_연차가_1_미만_80_초과일_경우_멘토_생성에_실패한다() {
        int lessThanOne = -3;
        int moreThanEighty = 81;

        assertThatThrownBy(() -> Mentor.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(refreshToken)
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(),  requiredInfoRequest.role()))
                .experiencedPositions(mentorRegisterInfoRequest.experiencedPositions())
                .careerContent(mentorRegisterInfoRequest.careerContent())
                .careerYear(lessThanOne)
                .build()).isInstanceOf(CustomException.class)
                .hasMessage("경력 연차가 올바르지 않습니다.");

        assertThatThrownBy(() -> Mentor.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(refreshToken)
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), requiredInfoRequest.role()))
                .experiencedPositions(mentorRegisterInfoRequest.experiencedPositions())
                .careerContent(mentorRegisterInfoRequest.careerContent())
                .careerYear(moreThanEighty)
                .build()).isInstanceOf(CustomException.class)
                .hasMessage("경력 연차가 올바르지 않습니다.");

    }

    @Test
    void 유효하지_않은_이메일_입력시_멘토_생성에_실패한다() {
        String invalidEmail = "dsklk3ssssssf@";

        assertThatThrownBy(() -> Mentor.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(invalidEmail)
                .refreshToken(refreshToken)
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), requiredInfoRequest.role()))
                .experiencedPositions(mentorRegisterInfoRequest.experiencedPositions())
                .careerContent(mentorRegisterInfoRequest.careerContent())
                .careerYear(mentorRegisterInfoRequest.careerYear())
                .build()).isInstanceOf(CustomException.class)
                .hasMessage("이메일이 유효하지 않습니다.");
    }

    @Test
    void 멘티_또는_관리자_역할로는_멘토_생성에_실패한다() {
        Role mentee = Role.ROLE_MENTEE;
        Role admin = Role.ROLE_ADMIN;

        assertThatThrownBy(() -> Mentor.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(refreshToken)
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), mentee))
                .experiencedPositions(mentorRegisterInfoRequest.experiencedPositions())
                .careerContent(mentorRegisterInfoRequest.careerContent())
                .careerYear(mentorRegisterInfoRequest.careerYear())
                .build()).isInstanceOf(CustomException.class)
                .hasMessage("허용되지 않은 역할입니다.");

        assertThatThrownBy(() -> Mentor.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(refreshToken)
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), admin))
                .experiencedPositions(mentorRegisterInfoRequest.experiencedPositions())
                .careerContent(mentorRegisterInfoRequest.careerContent())
                .careerYear(mentorRegisterInfoRequest.careerYear())
                .build()).isInstanceOf(CustomException.class)
                .hasMessage("허용되지 않은 역할입니다.");
    }

}
