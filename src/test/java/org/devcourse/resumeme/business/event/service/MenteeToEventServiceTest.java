package org.devcourse.resumeme.business.event.service;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.event.repository.MenteeToEventRepository;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenteeToEventServiceTest {

    @InjectMocks
    private MenteeToEventService menteeToEventService;

    @Mock
    private MenteeToEventRepository menteeToEventRepository;

    private Mentor mentorOne;

    private Mentor mentorTwo;

    private Mentee mentee;

    private Event eventOne;

    private Event eventTwo;

    @BeforeEach
    void setUp() {

        mentorOne =  Mentor.builder()
                .id(1L)
                .imageUrl("profile.png")
                .provider(Provider.valueOf("GOOGLE"))
                .email("progrers33@gmail.com")
                .refreshToken("redididkeeeeegg")
                .requiredInfo(new RequiredInfo("김주승", "주승멘토", "01022332375", Role.ROLE_MENTOR))
                .experiencedPositions(Set.of("FRONT"))
                .careerContent("금융회사 다님")
                .careerYear(3)
                .build();

        mentorTwo =  Mentor.builder()
                .id(2L)
                .imageUrl("profile.png")
                .provider(Provider.valueOf("GOOGLE"))
                .email("mentor222@gmail.com")
                .refreshToken("redididkeeeeegg")
                .requiredInfo(new RequiredInfo("김기안", "기안멘토", "01022632375", Role.ROLE_MENTOR))
                .experiencedPositions(Set.of("FRONT"))
                .careerContent("유통회사 다님")
                .careerYear(5)
                .build();

        mentee = Mentee.builder()
                .id(1L)
                .imageUrl("menteeimage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("backdong1@kakao.com")
                .refreshToken("ddefweferfrte")
                .requiredInfo(new RequiredInfo("김백둥", "백둥둥", "01022223722", Role.ROLE_MENTEE))
                .interestedPositions(Set.of())
                .interestedFields(Set.of())
                .introduce(null)
                .build();

        EventInfo openEvent = EventInfo.book(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.book(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L), LocalDateTime.now().plusHours(4L));
        eventOne = new Event(openEvent, eventTimeInfo, mentorOne, List.of());
        eventTwo = new Event(openEvent, eventTimeInfo, mentorTwo, List.of());
    }

    @Test
    void 하나의_이력서로_참여한_이벤트_목록_조회에_성공한다() {
        //given
        MenteeToEvent menteeToEventFirst = new MenteeToEvent(eventOne, 1L, 1L);
        MenteeToEvent menteeToEventSecond = new MenteeToEvent(eventTwo, 1L, 1L);

        given(menteeToEventRepository.findAllByResumeId(1L)).willReturn(List.of(menteeToEventFirst, menteeToEventSecond));

        //when
        List<MenteeToEvent> eventsRelatedToResume = menteeToEventService.getEventsRelatedToResume(1L);

        //then
        assertThat(eventsRelatedToResume.size()).isEqualTo(2);
    }

}
