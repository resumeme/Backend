package org.devcourse.resumeme.domain.event;

import org.devcourse.resumeme.domain.event.exception.EventException;
import org.devcourse.resumeme.domain.metor.Mentor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EventTest {

    private Event event;

    @BeforeEach
    void init() {
        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(2L));
        event = new Event(openEvent, eventTimeInfo, new Mentor(), List.of());
    }

    @Test
    void 이벤트_신청에_성공후_잔여석을_반환한다() {
        // when
        int remainSeats = event.acceptMentee(1L);

        // then
        assertThat(remainSeats).isEqualTo(2);
    }

    @Test
    void 잔여석이_남지않아서_이벤트신청에_실패한다() {
        // given
        event.acceptMentee(1L);
        event.acceptMentee(2L);
        event.acceptMentee(3L);

        // when & then
        assertThatThrownBy(() -> event.acceptMentee(4L))
                .isInstanceOf(EventException.class);
    }

    @Test
    void 중복신청으로_이벤트신청에_실패한다() {
        // given
        event.acceptMentee(1L);

        // when & then
        assertThatThrownBy(() -> event.acceptMentee(1L))
                .isInstanceOf(EventException.class);
    }

    @Test
    void 멘토는_멘티의_신청을_반려할수있다() {
        // given
        event.acceptMentee(1L);
        event.acceptMentee(2L);

        // when
        int remainSeats = event.reject(1L);

        // then
        assertThat(remainSeats).isEqualTo(2);
    }

    @Test
    void 잔여석이_생겨서_재오픈에_성공한다() {
        // given
        event.acceptMentee(1L);
        event.acceptMentee(2L);
        event.acceptMentee(3L);
        event.reject(1L);

        // when
        int remainSeats = event.reOpenEvent();

        // then
        assertThat(remainSeats).isEqualTo(1);
    }

    @Test
    void 잔여석이_없어_재오픈_신청에_실패한다() {
        // given
        event.acceptMentee(1L);
        event.acceptMentee(2L);
        event.acceptMentee(3L);

        // when & then
        assertThatThrownBy(() -> event.reOpenEvent())
                .isInstanceOf(EventException.class);
    }

    @Test
    void 오픈예약한_시간이_아니라서_이벤트_오픈에_실패한다() {
        // given
        EventInfo openEvent = EventInfo.book(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.book(LocalDateTime.now(), LocalDateTime.now().plusHours(2L), LocalDateTime.now().plusHours(4L));
        Event bookEvent = new Event(openEvent, eventTimeInfo, new Mentor(), List.of());

        // when & then
        assertThatThrownBy(() -> bookEvent.openReservationEvent(LocalDateTime.now()))
                .isInstanceOf(EventException.class);
    }

}
