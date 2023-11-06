package org.devcourse.resumeme.domain.resume;

import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.domain.user.Provider;
import org.devcourse.resumeme.domain.user.Role;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CareerTest {

    private String companyName;
    private String position;
    private Resume resume;
    private List<String> skills;
    private List<Duty> duties;
    private boolean isCurrentlyEmployed;
    private String careerContent;
    private Mentee mentee;

    @BeforeEach
    void init() {
        companyName = "Company";
        position = "BACK";
        skills = new ArrayList<>();
        duties = new ArrayList<>();
        isCurrentlyEmployed = true;
        careerContent = "상세 업무";
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
    void 종료일이_시작일_보다_먼저일_시_예외_발생() {
        isCurrentlyEmployed = false;
        LocalDate careerStartDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().minusYears(1);

        assertThatThrownBy(() -> new Career(companyName, position, resume, skills, duties, isCurrentlyEmployed, careerStartDate, endDate, careerContent))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 재직중일시_종료일_필수_입력() {
        isCurrentlyEmployed = false;
        LocalDate careerStartDate = LocalDate.now();
        LocalDate endDate = null;
        String careerContent = "상세 업무";

        assertThatThrownBy(() -> new Career(companyName, position, resume, skills, duties, isCurrentlyEmployed, careerStartDate, endDate, careerContent))
                .isInstanceOf(CustomException.class);
    }

}
