package org.devcourse.resumeme.business.resume.domain;

import org.devcourse.resumeme.business.resume.domain.career.Career;
import org.devcourse.resumeme.business.resume.domain.career.Duty;
import org.devcourse.resumeme.global.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CareerTest {

    private String companyName;
    private String position;
    private List<String> skills;
    private List<Duty> duties;
    private String careerContent;

    @BeforeEach
    void init() {
        companyName = "Company";
        position = "BACK";
        skills = new ArrayList<>();
        duties = new ArrayList<>();
        careerContent = "상세 업무";
    }

    @Test
    void 종료일이_시작일_보다_먼저일_시_예외_발생() {
        LocalDate careerStartDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().minusYears(1);

        assertThatThrownBy(() -> new Career(companyName, position, skills, duties, careerStartDate, endDate, careerContent))
                .isInstanceOf(CustomException.class);
    }

}
