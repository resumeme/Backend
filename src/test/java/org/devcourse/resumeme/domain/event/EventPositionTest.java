package org.devcourse.resumeme.domain.event;

import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.domain.metor.Mentor;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EventPositionTest {

    @ParameterizedTest
    @MethodSource("positionAndEvent")
    void 이벤트포지션_생성_검증조건_미달로인해_생성에_실패한다_null입력_오류(Position position, Event event) {
        // then
        assertThatThrownBy(() -> new EventPosition(position, event))
                .isInstanceOf(CustomException.class);
    }

    static Stream<Arguments> positionAndEvent() {
        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(2L));
        return Stream.of(
                Arguments.of(Position.BACK, null),
                Arguments.of(null, new Event(openEvent, eventTimeInfo, new Mentor(), List.of())),
                Arguments.of(null, null)
        );
    }

}
