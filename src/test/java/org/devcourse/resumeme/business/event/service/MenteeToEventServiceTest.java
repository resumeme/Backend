package org.devcourse.resumeme.business.event.service;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.event.repository.MenteeToEventRepository;
import org.devcourse.resumeme.business.event.service.vo.AcceptMenteeToEvent;
import org.devcourse.resumeme.business.resume.service.ResumeCopier;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
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
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenteeToEventServiceTest {

    @InjectMocks
    private MenteeToEventService menteeToEventService;

    @Mock
    private ResumeCopier resumeCopier;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private MenteeToEventRepository menteeToEventRepository;

    private ExecutorService executorService;

    private CountDownLatch countDownLatch;

    private AtomicInteger successCount;

    private AtomicInteger failCount;

    int executeCount;

    @BeforeEach
    void setUp() {
        executeCount = 10;
        executorService = Executors.newFixedThreadPool(3);
        countDownLatch = new CountDownLatch(executeCount);
        successCount = new AtomicInteger();
        failCount = new AtomicInteger();

    }

    @Test
    void 여러스레드를_이용하여_선착순으로_진행한_이벤트참여신청에_최대_참여수만큼_성공한다() throws InterruptedException {
        // given
        EventInfo openEvent = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        Event event = new Event(openEvent, eventTimeInfo, 1L, List.of());

        given(eventRepository.findWithLockById(1L)).willReturn(Optional.of(event));

        // when
        for (int i = 0; i < executeCount; i++) {
            executeThread((number) -> menteeToEventService.acceptMentee(new AcceptMenteeToEvent(1L, Long.valueOf(number), 1L)), i);
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

}
