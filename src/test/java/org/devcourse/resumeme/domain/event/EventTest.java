package org.devcourse.resumeme.domain.event;

import org.devcourse.resumeme.domain.event.exception.EventException;
import org.devcourse.resumeme.domain.mentor.Mentor;
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
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        event = new Event(openEvent, eventTimeInfo, new Mentor(), List.of());
    }

    @Test
    void 이벤트_신청에_성공후_잔여석을_반환한다() {
        // when
        int remainSeats = event.acceptMentee(1L, 1L);

        // then
        assertThat(remainSeats).isEqualTo(2);
    }

    @Test
    void 잔여석이_남지않아서_이벤트신청에_실패한다() {
        // given
        event.acceptMentee(1L, 1L);
        event.acceptMentee(2L, 1L);
        event.acceptMentee(3L, 1L);

        // when & then
        assertThatThrownBy(() -> event.acceptMentee(4L, 1L))
                .isInstanceOf(EventException.class);
    }

    @Test
    void 중복신청으로_이벤트신청에_실패한다() {
        // given
        event.acceptMentee(1L, 1L);

        // when & then
        assertThatThrownBy(() -> event.acceptMentee(1L, 1L))
                .isInstanceOf(EventException.class);
    }

    @Test
    void 멘토는_멘티의_신청을_반려할수있다() {
        // given
        event.acceptMentee(1L, 1L);
        event.acceptMentee(2L, 1L);

        // when
        int remainSeats = event.reject(1L, "message");

        // then
        assertThat(remainSeats).isEqualTo(2);
    }

    @Test
    void 잔여석이_생겨서_재오픈에_성공한다() {
        // given
        event.acceptMentee(1L, 1L);
        event.acceptMentee(2L, 1L);
        event.acceptMentee(3L, 1L);
        event.reject(1L, "message");

        // when
        int remainSeats = event.reOpenEvent();

        // then
        assertThat(remainSeats).isEqualTo(1);
    }

    @Test
    void 잔여석이_없어_재오픈_신청에_실패한다() {
        // given
        event.acceptMentee(1L, 1L);
        event.acceptMentee(2L, 1L);
        event.acceptMentee(3L, 1L);

        // when & then
        assertThatThrownBy(() -> event.reOpenEvent())
                .isInstanceOf(EventException.class);
    }

    @Test
    void 오픈예약한_시간이_아니라서_이벤트_오픈에_실패한다() {
        // given
        EventInfo openEvent = EventInfo.book(3, "제목", "내용");
        LocalDateTime now = LocalDateTime.now();
        EventTimeInfo eventTimeInfo = EventTimeInfo.book(now, LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        Event bookEvent = new Event(openEvent, eventTimeInfo, new Mentor(), List.of());

        // when & then
        assertThatThrownBy(() -> bookEvent.openReservationEvent(now))
                .isInstanceOf(EventException.class);
    }

    @Test
    void 참여자를_반려시키고_잔여자리가_한자리_늘어난다() {
        // given
        for (long i = 0; i < 2; i++) {
            event.acceptMentee(i, i);
        }

        // when
        int remainSeat = event.reject(1L, "message");

        // then
        assertThat(remainSeat).isEqualTo(2);
    }

    @Test
    void 참여하지않은_멘티_아이디로는_반려를_할수없다() {
        // given
        for (long i = 0; i < 2; i++) {
            event.acceptMentee(i, i);
        }

        // when
        int remainSeat = event.reject(3L, "message");

        // then
        assertThat(remainSeat).isEqualTo(1);
    }

}
