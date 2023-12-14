package org.devcourse.resumeme.business.event.service;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.event.exception.EventException;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.devcourse.resumeme.business.event.service.listener.EventCreationPublisher;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.service.UserInfoProvider;
import org.devcourse.resumeme.business.user.service.UserService;
import org.devcourse.resumeme.business.user.service.vo.UserResponse;
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
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.devcourse.resumeme.business.event.service.listener.EventCreation.EventNoticeInfo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EventServiceTest {

    @Mock
    private EventCreationPublisher eventCreationPublisher;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserInfoProvider userInfoProvider;

    @InjectMocks
    private EventService eventService;


    int executeCount;

    private Mentor mentor;

    @BeforeEach
    void init() {
        executeCount = 10;

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
        Event event = new Event(openEvent, eventTimeInfo, 1L, List.of());

        given(userInfoProvider.getOne(1L)).willReturn(new UserResponse(1L, "nickname", "name", "email", "01012345678", "url"));
        given(eventRepository.findAllByMentorId(1L)).willReturn(List.of());
        given(eventRepository.save(event)).willReturn(event);
        doNothing().when(eventCreationPublisher).publishEventCreation(any(EventNoticeInfo.class));

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
        Event event = new Event(openEvent, eventTimeInfo, 1L, List.of());

        EventInfo openEvent1 = EventInfo.open(3, "제목", "내용");
        EventTimeInfo eventTimeInfo1 = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        Event event1 = new Event(openEvent1, eventTimeInfo1, 1L, List.of());

        given(eventRepository.findAllByMentorId(1L)).willReturn(List.of(event1));

        // when & then
        assertThatThrownBy(() -> eventService.create(event))
                .isInstanceOf(EventException.class);
    }

}
