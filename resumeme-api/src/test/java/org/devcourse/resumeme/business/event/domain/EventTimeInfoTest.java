package org.devcourse.resumeme.business.event.domain;

import org.devcourse.resumeme.business.event.exception.EventException;
import org.devcourse.resumeme.global.exception.CustomException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EventTimeInfoTest {

    @Test
    void 이벤트_시간정보_생성에_성공한다() {
        // given
        EventTimeInfo onStart = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        EventTimeInfo book = EventTimeInfo.book(LocalDateTime.now(), LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3));

        // then
        assertThat(onStart).isNotNull();
        assertThat(book).isNotNull();
    }

    @Test
    void 이벤트_시간_예약시_오픈시간이_현재시간보다_빠르면_예약에_실패한다() {
        // then
        assertThatThrownBy(() -> EventTimeInfo.book(LocalDateTime.now(), LocalDateTime.now().minusSeconds(1), LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3)))
                .isInstanceOf(EventException.class);
    }

    @ParameterizedTest
    @MethodSource("timeInfo")
    void 이벤트_시간_생성_검증조건에_실패하여_생성에_실패한다(LocalDateTime openDateTime, LocalDateTime closeDateTime, LocalDateTime endDate) {
        // then
        assertThatThrownBy(() -> EventTimeInfo.onStart(LocalDateTime.now(), closeDateTime, endDate))
                .isInstanceOf(CustomException.class);

        assertThatThrownBy(() -> EventTimeInfo.book(LocalDateTime.now(), openDateTime, closeDateTime, endDate))
                .isInstanceOf(CustomException.class);
    }

    static Stream<Arguments> timeInfo() {
        return Stream.of(
                Arguments.of(null, null, null),
                Arguments.of(LocalDateTime.now(), null, null),
                Arguments.of(null, LocalDateTime.now(), null),
                Arguments.of(null, null, LocalDateTime.now()),
                Arguments.of(LocalDateTime.now().plusHours(2), LocalDateTime.now().minusHours(1), null),
                Arguments.of(null, LocalDateTime.now().plusHours(2), LocalDateTime.now().minusHours(1))
        );
    }

}
