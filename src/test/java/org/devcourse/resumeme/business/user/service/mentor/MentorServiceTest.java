package org.devcourse.resumeme.business.user.service.mentor;

import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.repository.mentor.MentorRepository;
import org.devcourse.resumeme.business.user.service.admin.MentorApplicationEventPublisher;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MentorServiceTest {

    @InjectMocks
    private MentorService mentorService;

    @Mock
    private MentorRepository mentorRepository;

    @Mock
    private MentorApplicationEventPublisher mentorApplicationEventPublisher;

    private Mentor mentor;

    private Mentor savedMentor;

    @Test
    void 멘토_생성에_성공한다() {
        // given
        mentor = Mentor.builder()
                .imageUrl("profileImage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("happy123@naver.com")
                .requiredInfo(new RequiredInfo("박철수", "fePark", "01038337266", Role.ROLE_PENDING))
                .experiencedPositions(Set.of("FRONT", "BACK"))
                .careerContent("5년차 멍멍이 넥카라 개발자")
                .careerYear(5)
                .build();

        savedMentor = Mentor.builder()
                .id(1L)
                .imageUrl("profileImage.png")
                .provider(Provider.valueOf("KAKAO"))
                .email("happy123@naver.com")
                .requiredInfo(new RequiredInfo("박철수", "fePark", "01038337266", Role.ROLE_PENDING))
                .experiencedPositions(Set.of("FRONT", "BACK"))
                .careerContent("5년차 멍멍이 넥카라 개발자")
                .careerYear(5)
                .build();

        given(mentorRepository.save(any(Mentor.class))).willReturn(savedMentor);
        doNothing().when(mentorApplicationEventPublisher).publishMentorApplicationEvent(savedMentor);

        // when
        assertThat(mentorService.create(mentor)).usingRecursiveComparison()
                .isEqualTo(savedMentor);
    }

}
