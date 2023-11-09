package org.devcourse.resumeme.business.resume.domain;

import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TrainingTest {

    private Resume resume;

    private Mentee mentee;

    @BeforeEach
    void init() {
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
        resume = new Resume("title", mentee);
    }

    @Test
    void 입학일자는_졸업일자_보다_먼저여야_한다() {
        assertThatThrownBy(() -> new Training("School", "Major", "Degree",
                LocalDate.now(), LocalDate.now().minusDays(1), 3.5, 4.0, "Explanation"))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 최대학점은_내_학점보다_커야한다() {
        double gpa = 5.5;
        double maxGpa = 4.0;

        assertThatThrownBy(() -> new Training("School", "Major", "Degree",
                LocalDate.now(), LocalDate.now().plusDays(1), gpa, maxGpa, "Explanation"))
                .isInstanceOf(CustomException.class);
    }

}
