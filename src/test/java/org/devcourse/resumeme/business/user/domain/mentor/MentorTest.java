package org.devcourse.resumeme.business.user.domain.mentor;

import org.assertj.core.api.Assertions;
import org.devcourse.resumeme.business.user.controller.dto.RequiredInfoRequest;
import org.devcourse.resumeme.business.user.controller.dto.mentor.MentorRegisterInfoRequest;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;
import org.devcourse.resumeme.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.devcourse.resumeme.business.user.domain.Role.ROLE_ADMIN;
import static org.devcourse.resumeme.business.user.domain.Role.ROLE_MENTEE;
import static org.devcourse.resumeme.business.user.domain.Role.ROLE_MENTOR;
import static org.devcourse.resumeme.business.user.domain.Role.ROLE_PENDING;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MentorTest {

    private RequiredInfoRequest requiredInfoRequest;

    private MentorRegisterInfoRequest mentorRegisterInfoRequest;

    private OAuth2TempInfo oAuth2TempInfo;

    private String refreshToken;

    @BeforeEach
    void SetUP() {
        requiredInfoRequest = new RequiredInfoRequest("nickname", "박백둥", "01034548443", "pending");
        mentorRegisterInfoRequest = new MentorRegisterInfoRequest("cacheKey", requiredInfoRequest, Set.of("FRONT", "BACK"), "A회사 00팀, B회사 xx팀", 3, "안녕하세요 멘토가 되고싶어요.");
        oAuth2TempInfo = new OAuth2TempInfo(null, "GOOGLE", "지롱", "devcoco@naver.com", "image.png");
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
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), Role.of(requiredInfoRequest.role())))
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
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), Role.of(requiredInfoRequest.role())))
                .experiencedPositions(emptyExperiencedPositions)
                .careerContent(mentorRegisterInfoRequest.careerContent())
                .careerYear(mentorRegisterInfoRequest.careerYear())
                .build()).isInstanceOf(CustomException.class)
                .hasMessage("빈 값일 수 없습니다");
    }

    @Test
    void 경력_연차가_1_미만_80_초과일_경우_멘토_생성에_실패한다() {
        int lessThanOne = 0;
        int moreThanEighty = 81;

        assertThatThrownBy(() -> Mentor.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(refreshToken)
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), Role.of(requiredInfoRequest.role())))
                .experiencedPositions(mentorRegisterInfoRequest.experiencedPositions())
                .careerContent(mentorRegisterInfoRequest.careerContent())
                .careerYear(lessThanOne)
                .build()).isInstanceOf(CustomException.class)
                .hasMessage("경력 연차가 올바르지 않습니다");

        assertThatThrownBy(() -> Mentor.builder()
                .id(1L)
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider()))
                .email(oAuth2TempInfo.getEmail())
                .refreshToken(refreshToken)
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), Role.of(requiredInfoRequest.role())))
                .experiencedPositions(mentorRegisterInfoRequest.experiencedPositions())
                .careerContent(mentorRegisterInfoRequest.careerContent())
                .careerYear(moreThanEighty)
                .build()).isInstanceOf(CustomException.class)
                .hasMessage("경력 연차가 올바르지 않습니다");

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
                .requiredInfo(new RequiredInfo(requiredInfoRequest.realName(), requiredInfoRequest.nickname(), requiredInfoRequest.phoneNumber(), Role.of(requiredInfoRequest.role())))
                .experiencedPositions(mentorRegisterInfoRequest.experiencedPositions())
                .careerContent(mentorRegisterInfoRequest.careerContent())
                .careerYear(mentorRegisterInfoRequest.careerYear())
                .build()).isInstanceOf(CustomException.class)
                .hasMessage("이메일이 유효하지 않습니다");
    }

    @Test
    void 신규_가입_멘티의_ROLE을_PENDING에서_MENTOR로_수정할_수_있다() {
        // given
        Mentor mentor = Mentor.builder()
                .id(1L)
                .imageUrl("profileImage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("happy123@naver.com")
                .requiredInfo(new RequiredInfo("박철수", "fePark", "01038337266", ROLE_PENDING))
                .experiencedPositions(Set.of("FRONT", "BACK"))
                .careerContent("5년차 멍멍이 넥카라 개발자")
                .careerYear(5)
                .build();

        // when
        mentor.updateRole(ROLE_MENTOR);

        // then
        Assertions.assertThat(mentor.getRequiredInfo().getRole()).isEqualTo(ROLE_MENTOR);
    }

    @Test
    void 멘토의_ROLE을_MENTEE로_수정하는데_실패한다() {
        // given
        Mentor mentor = Mentor.builder()
                .id(1L)
                .imageUrl("profileImage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("happy123@naver.com")
                .requiredInfo(new RequiredInfo("박철수", "fePark", "01038337266", ROLE_PENDING))
                .experiencedPositions(Set.of("FRONT", "BACK"))
                .careerContent("5년차 멍멍이 넥카라 개발자")
                .careerYear(5)
                .build();

        assertThatThrownBy(() -> mentor.updateRole(ROLE_MENTEE)).isInstanceOf(CustomException.class);
    }

    @Test
    void 멘토의_ROLE을_ADMIN으로_수정하는데_실패한다() {
        // given
        Mentor mentor = Mentor.builder()
                .id(1L)
                .imageUrl("profileImage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("happy123@naver.com")
                .requiredInfo(new RequiredInfo("박철수", "fePark", "01038337266", ROLE_PENDING))
                .experiencedPositions(Set.of("FRONT", "BACK"))
                .careerContent("5년차 멍멍이 넥카라 개발자")
                .careerYear(5)
                .build();

        assertThatThrownBy(() -> mentor.updateRole(ROLE_ADMIN)).isInstanceOf(CustomException.class);
    }

    @Test
    void 이미_승인된_멘토는_승인여부_확인시_true를_반환한다() {
        // given
        Mentor mentor = Mentor.builder()
                .id(1L)
                .imageUrl("profileImage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("happy123@naver.com")
                .requiredInfo(new RequiredInfo("박철수", "fePark", "01038337266", ROLE_MENTOR))
                .experiencedPositions(Set.of("FRONT", "BACK"))
                .careerContent("5년차 멍멍이 넥카라 개발자")
                .careerYear(5)
                .build();

        // when, then
        Assertions.assertThat(mentor.isApproved()).isTrue();
    }

    @Test
    void 미승인_상태의_멘토는_승인여부_확인시_false를_반환한다() {
        // given
        Mentor mentor = Mentor.builder()
                .id(1L)
                .imageUrl("profileImage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("happy123@naver.com")
                .requiredInfo(new RequiredInfo("박철수", "fePark", "01038337266", ROLE_PENDING))
                .experiencedPositions(Set.of("FRONT", "BACK"))
                .careerContent("5년차 멍멍이 넥카라 개발자")
                .careerYear(5)
                .build();

        // when, then
        Assertions.assertThat(mentor.isApproved()).isFalse();
    }
}
