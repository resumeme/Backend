package org.devcourse.resumeme.business.event.service;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventInfo;
import org.devcourse.resumeme.business.event.domain.EventPosition;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.event.repository.EventPositionRepository;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.common.domain.Position;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class EventPositionServiceTest {

    @Mock
    private EventPositionRepository repository;

    @InjectMocks
    private EventPositionService eventPositionService;

    @Test
    void 전체_조회_테스트() throws IllegalAccessException, NoSuchFieldException {
        // given
        Mentor mentor = Mentor.builder()
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

        EventInfo openEvent = EventInfo.book(3, "제목", "내용");
        EventTimeInfo eventTimeInfo = EventTimeInfo.book(LocalDateTime.now(), LocalDateTime.now().plusHours(1L), LocalDateTime.now().plusHours(2L), LocalDateTime.now().plusHours(4L));
        Event event = new Event(openEvent, eventTimeInfo, 1L, List.of());
        Field field = event.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(event, 1L);

        given(repository.findAllByEventIds(List.of(1L))).willReturn(List.of(new EventPosition(Position.BACK, event, 1), new EventPosition(Position.DEVOPS, event, 2)));

        // when
        List<EventPosition> positions = eventPositionService.getAll(List.of(1L));

        // then
        assertThat(positions).hasSize(2);
    }


}
