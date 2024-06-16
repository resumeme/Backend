package org.devcourse.resumeme.business.event.domain;

import org.devcourse.resumeme.business.event.exception.EventException;
import org.devcourse.resumeme.global.exception.CustomException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.devcourse.resumeme.business.event.domain.EventStatus.CLOSE;
import static org.devcourse.resumeme.business.event.domain.EventStatus.OPEN;
import static org.devcourse.resumeme.business.event.domain.EventStatus.REOPEN;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EventInfoTest {

    @Test
    void 이벤트_생성에_성공한다() {
        // given
        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventInfo bookEvent = EventInfo.book(3, "제목", "내용");

        // then
        assertThat(openEvent).usingRecursiveComparison()
                .comparingOnlyFields("status")
                .isEqualTo(OPEN);

        assertThat(bookEvent).usingRecursiveComparison()
                .comparingOnlyFields("status")
                .isEqualTo(CLOSE);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 11})
    void 이벤트_생성_검증조건_미달로인해_생성에_실패한다_최대참석자수_범위_오류(int maximumAttendee) {
        // then
        assertThatThrownBy(() -> EventInfo.open(maximumAttendee, "제목", "내용"))
                .isInstanceOf(CustomException.class);

        assertThatThrownBy(() -> EventInfo.book(maximumAttendee, "제목", "내용"))
                .isInstanceOf(CustomException.class);
    }

    @ParameterizedTest
    @MethodSource("titleAndContent")
    void 이벤트_생성_검증조건_미달로인해_생성에_실패한다_제목_내용_오류(String title, String content) {
        // then
        assertThatThrownBy(() -> EventInfo.open(3, title, content))
                .isInstanceOf(CustomException.class);

        assertThatThrownBy(() -> EventInfo.book(3, title, content))
                .isInstanceOf(CustomException.class);
    }

    static Stream<Arguments> titleAndContent() {
        return Stream.of(
                Arguments.of("제목", ""),
                Arguments.of("", "내용"),
                Arguments.of("제목", null),
                Arguments.of(null, "내용"),
                Arguments.of(null, null)
        );
    }

    @Test
    void 아직_이벤트가_오픈상태이다() {
        // given
        EventInfo eventInfo = EventInfo.open(3, "제목", "내용");

        // when
        eventInfo.checkAvailableApplication();

        //then
        assertThat(eventInfo).usingRecursiveComparison()
                .comparingOnlyFields("status")
                .isEqualTo(OPEN);
    }

    @Test
    void 이벤트가_마감상태이므로_예외를_반환한다() {
        // given
        EventInfo eventInfo = EventInfo.open(3, "제목", "내용");

        // when
        eventInfo.close(3);

        // then
        assertThatThrownBy(eventInfo::checkAvailableApplication)
                .isInstanceOf(EventException.class);
        assertThat(eventInfo).usingRecursiveComparison()
                .comparingOnlyFields("status")
                .isEqualTo(CLOSE);
    }

    @Test
    void 아직_잔여자리가_남아있어_이벤트를_오픈상태로_유지한다() {
        // given
        EventInfo eventInfo = EventInfo.open(3, "제목", "내용");

        // when
        int remainSeats = eventInfo.close(1);

        // then
        assertThat(remainSeats).isEqualTo(2);
        assertThat(eventInfo).usingRecursiveComparison()
                .comparingOnlyFields("status")
                .isEqualTo(OPEN);
    }

    @Test
    void 잔여자리가_없어_이벤트를_마감상태로_변경한다() {
        // given
        EventInfo eventInfo = EventInfo.open(3, "제목", "내용");

        // when
        int remainSeats = eventInfo.close(3);

        // then
        assertThat(remainSeats).isZero();
        assertThat(eventInfo).usingRecursiveComparison()
                .comparingOnlyFields("staus")
                .isEqualTo(CLOSE);
    }

    @Test
    void 멘토의_요청으로인해_이벤트모집이_재오픈상태로_변경된다() {
        // given
        EventInfo eventInfo = EventInfo.open(3, "제목", "내용");
        eventInfo.close(3);

        // when
        eventInfo.reOpen(2);

        // then
        assertThat(eventInfo).usingRecursiveComparison()
                .comparingOnlyFields("status")
                .isEqualTo(REOPEN);
    }

    @Test
    void 잔여_자리가_없어_재오픈_요청에_대해_예외를_반환한다() {
        // given
        EventInfo eventInfo = EventInfo.open(3, "제목", "내용");
        eventInfo.close(3);

        // when & then
        assertThatThrownBy(() -> eventInfo.reOpen(3))
                .isInstanceOf(EventException.class);
        assertThat(eventInfo).usingRecursiveComparison()
                .comparingOnlyFields("status")
                .isEqualTo(CLOSE);
    }

    @Test
    void 예약한_이벤트를_오픈상태로_변경한다() {
        // given
        EventInfo eventInfo = EventInfo.book(3, "제목", "내용");

        // when
        eventInfo.open();

        // then
        assertThat(eventInfo).usingRecursiveComparison()
                .comparingOnlyFields("status")
                .isEqualTo(OPEN);
    }

    @Test
    void 예약한_이벤트가_아니므로_오픈상태로_변경시_예외를_발생한다() {
        // given
        EventInfo eventInfo = EventInfo.open(3, "제목", "내용");

        // when & then
        assertThatThrownBy(eventInfo::open)
                .isInstanceOf(EventException.class);
    }

}
