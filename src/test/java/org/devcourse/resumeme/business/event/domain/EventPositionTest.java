package org.devcourse.resumeme.business.event.domain;

import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.global.exception.CustomException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EventPositionTest {

    @ParameterizedTest
    @MethodSource("positionAndEvent")
    void 이벤트포지션_생성_검증조건_미달로인해_생성에_실패한다_null입력_오류(Position position, Event event) {
        // then
        assertThatThrownBy(() -> new EventPosition(position, event, 1))
                .isInstanceOf(CustomException.class);
    }

    static Stream<Arguments> positionAndEvent() {
        Mentor mentor =  Mentor.builder()
                .id(1L)
                .imageUrl("profile.png")
                .provider(Provider.valueOf("GOOGLE"))
                .email("programmers33@gmail.com")
                .refreshToken("redididkeeeeegg")
                .requiredInfo(new RequiredInfo("김주승", "주승멘토", "01022332375", Role.ROLE_MENTOR))
                .experiencedPositions(Set.of("DEVOPS"))
                .careerContent("금융회사 다님")
                .careerYear(3)
                .build();

        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        return Stream.of(
                Arguments.of(Position.BACK, null),
                Arguments.of(null, new Event(openEvent, eventTimeInfo, 1L, List.of())),
                Arguments.of(null, null)
        );
    }

}
