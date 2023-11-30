package org.devcourse.resumeme.business.event.domain;

import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.global.exception.CustomException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenteeToEventTest {

    @Test
    void 멘티와_이벤트를_연결하는_연관관계테이블_객체_생성에_성공한다() {
        Mentor mentor =  Mentor.builder()
                .id(1L)
                .imageUrl("profile.png")
                .provider(Provider.valueOf("GOOGLE"))
                .email("programmers33@gmail.com")
                .refreshToken("redididkeeeeegg")
                .requiredInfo(new RequiredInfo("김주승", "주승멘토", "01022332375", Role.ROLE_MENTOR))
                .experiencedPositions(Set.of("BACK"))
                .careerContent("금융회사 다님")
                .careerYear(3)
                .build();
        // given
        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        MenteeToEvent menteeToEvent = new MenteeToEvent(new Event(openEvent, eventTimeInfo, 1L, List.of()), 1L, 1L);

        // then
        assertThat(menteeToEvent).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("eventMenteeID")
    void 멘티와_이벤트를_연결하는_연관관계테이블_객체_생성에_실패한다_검증조건실패(Event event, Long menteeId) {
        // then
        assertThatThrownBy(() -> new MenteeToEvent(event, menteeId, 1L))
                .isInstanceOf(CustomException.class);
    }

    static Stream<Arguments> eventMenteeID() {
        Mentor mentor =  Mentor.builder()
                .id(1L)
                .imageUrl("profile.png")
                .provider(Provider.valueOf("GOOGLE"))
                .email("programmers33@gmail.com")
                .refreshToken("redididkeeeeegg")
                .requiredInfo(new RequiredInfo("김주승", "주승멘토", "01022332375", Role.ROLE_MENTOR))
                .experiencedPositions(Set.of("BACK"))
                .careerContent("금융회사 다님")
                .careerYear(3)
                .build();

        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(null, 1L),
                Arguments.of(new Event(openEvent, eventTimeInfo, 1L, List.of()), null)
        );
    }

    @ParameterizedTest
    @MethodSource("sameMentee")
    void 현재_신청한_멘티와_일치여부를_확인한다(Long menteeId, boolean result) {
        Mentor mentor =  Mentor.builder()
                .id(1L)
                .imageUrl("profile.png")
                .provider(Provider.valueOf("GOOGLE"))
                .email("programmers33@gmail.com")
                .refreshToken("redididkeeeeegg")
                .requiredInfo(new RequiredInfo("김주승", "주승멘토", "01022332375", Role.ROLE_MENTOR))
                .experiencedPositions(Set.of("BACK"))
                .careerContent("금융회사 다님")
                .careerYear(3)
                .build();

        // given
        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        MenteeToEvent menteeToEvent = new MenteeToEvent(new Event(openEvent, eventTimeInfo, 1L, List.of()), 1L, 1L);

        // when
        boolean sameMentee = menteeToEvent.isSameMentee(menteeId);

        // then
        assertThat(sameMentee).isEqualTo(result);
    }

    static Stream<Arguments> sameMentee() {
        return Stream.of(
                Arguments.of(1L, true),
                Arguments.of(2L, false)
        );
    }

}
