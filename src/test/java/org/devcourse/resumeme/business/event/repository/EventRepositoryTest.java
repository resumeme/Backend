package org.devcourse.resumeme.business.event.repository;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.repository.mentor.MentorRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.devcourse.resumeme.common.domain.Position.FRONT;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private EntityManager em;

    @Test
    void 멘티는_자신의_이벤트참여_이력서를_찾을_수_있다() {
        // given
        Mentor mentor = Mentor.builder()
                .id(2L)
                .imageUrl("profileImage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("happy123@naver.com")
                .requiredInfo(new RequiredInfo("박철수", "fePark", "01038337266", Role.ROLE_PENDING))
                .experiencedPositions(Set.of("FRONT", "BACK"))
                .careerContent("5년차 멍멍이 넥카라 개발자")
                .careerYear(5)
                .build();
        mentorRepository.save(mentor);

        EventInfo openEventOne = EventInfo.open(3, "수증 멘토의 첫번째 첨삭이벤트", "내용");
        EventTimeInfo eventOneTimeInfo = EventTimeInfo.onStart(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L));
        Event event = new Event(openEventOne, eventOneTimeInfo, mentor, List.of(FRONT));

        Event event2 = new Event(openEventOne, eventOneTimeInfo, mentor, List.of(FRONT));
        event.acceptMentee(1L, 1L);
        event2.acceptMentee(1L, 2L);
        event.acceptMentee(2L, 4L);
        eventRepository.save(event);
        eventRepository.save(event2);

        em.flush();
        em.clear();

        // when
        List<Event> events = eventRepository.findAllByApplicantsResumeIds(List.of(1L, 2L));

        // then
        assertThat(events).hasSize(2);
        assertThat(events.get(0).getApplicants()).hasSize(1);
    }

}
