package org.devcourse.resumeme.business.event.service;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.event.exception.EventException;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.event.service.vo.AcceptMenteeToEvent;
import org.devcourse.resumeme.business.event.service.vo.EventReject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private ExecutorService executorService;

    private CountDownLatch countDownLatch;

    private AtomicInteger successCount;

    private AtomicInteger failCount;

    int executeCount;

    private Mentor mentor;

    @BeforeEach
    void init() {
        executeCount = 10;
        executorService = Executors.newFixedThreadPool(3);
        countDownLatch = new CountDownLatch(executeCount);
        successCount = new AtomicInteger();
        failCount = new AtomicInteger();

        mentor =  Mentor.builder()
                .id(1L)
                .imageUrl("profile.png")
                .provider(Provider.valueOf("GOOGLE"))
                .email("programmers33@gmail.com")
                .refreshToken("redididkeeeeegg")
                .requiredInfo(new RequiredInfo("김주승", "주승멘토", "01022332375", Role.ROLE_MENTOR))
                .experiencedPositions(Set.of("BACK", "FRONT"))
                .careerContent("금융회사 다님")
                .careerYear(3)
                .build();
    }

    @Test
    void 이벤트_생성에_성공한다() {
        // given
        EventInfo openEvent = EventInfo.book(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.book(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L), LocalDateTime.now().plusHours(4L));
        Event event = new Event(openEvent, eventTimeInfo, mentor, List.of());

        given(eventRepository.findAllByMentor(mentor)).willReturn(List.of());
        given(eventRepository.save(event)).willReturn(event);

        // when
        eventService.create(event);

        // then
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void 이미_오픈되어있는_이벤트가있어_이벤트_생성에_실패한다() {
        // given
        EventInfo openEvent = EventInfo.book(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.book(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L), LocalDateTime.now().plusHours(4L));
        Event event = new Event(openEvent, eventTimeInfo, mentor, List.of());

        EventInfo openEvent1 = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo1 = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        Event event1 = new Event(openEvent1, eventTimeInfo1, mentor, List.of());

        given(eventRepository.findAllByMentor(mentor)).willReturn(List.of(event1));

        // when & then
        assertThatThrownBy(() -> eventService.create(event))
                .isInstanceOf(EventException.class);
    }


    @Test
    void 여러스레드를_이용하여_선착순으로_진행한_이벤트참여신청에_최대_참여수만큼_성공한다() throws InterruptedException {
        // given
        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        Event event = new Event(openEvent, eventTimeInfo, mentor, List.of());

        given(eventRepository.findWithLockById(1L)).willReturn(Optional.of(event));

        // when
        for (int i = 0; i < executeCount; i++) {
            executeThread((number) -> eventService.acceptMentee(new AcceptMenteeToEvent(1L, Long.valueOf(number), 1L)), i);
        }

        countDownLatch.await();

        // then
        assertThat(successCount.get()).isEqualTo(3);
        assertThat(failCount.get()).isEqualTo(executeCount - 3);
    }

    private void executeThread(Consumer<Integer> service, int number) {
            executorService.execute(() -> {
                try {
                    Thread.sleep(new Random().nextLong(100));

                    service.accept(number);

                    successCount.getAndIncrement();
                } catch (Exception e) {
                    failCount.getAndIncrement();
                    System.out.println(e.getMessage());
                }
                countDownLatch.countDown();
            });
    }

    @Test
    void 이벤트_신청을_반려에_성공한다() {
        // given
        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        Event event = new Event(openEvent, eventTimeInfo, mentor, List.of());
        event.acceptMentee(1L, 1L);

        EventReject reject = new EventReject(1L, 1L, "이력서 양이 너무 적습니다");
        lenient().when(eventRepository.findWithApplicantsById(1L)).thenReturn(Optional.of(event));

        // when
        eventService.reject(reject);

        // then
        verify(eventRepository).findWithApplicantsById(1L);
    }

    @Test
    void 이벤트_신청을_반려에_실패한다_신청한_멘티가_없음() {
        // given
        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        Event event = new Event(openEvent, eventTimeInfo, mentor, List.of());
        event.acceptMentee(1L, 1L);

        EventReject reject = new EventReject(1L, 2L, "이력서 양이 너무 적습니다");
        given(eventRepository.findWithApplicantsById(1L)).willReturn(Optional.of(event));

        // when & then
        assertThatThrownBy(() -> eventService.reject(reject)).isInstanceOf(EventException.class);
    }

}
