package org.devcourse.resumeme.domain.resume;

import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CareerTest {

    @Test
    void 기술_스택_및_업무_내용_없을_시_예외_발생() {
        String companyName = "Company";
        List<Skill> skills = new ArrayList<>();
        List<Duty> duties = new ArrayList<>();
        LocalDate careerStartDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(2);
        String careerContent = "상세 업무";

        assertThatThrownBy(() -> new Career(companyName, skills, duties, careerStartDate, endDate, careerContent))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 종료일이_시작일_보다_먼저일_시_예외_발생() {
        String companyName = "Company";
        List<Skill> skills = new ArrayList<>();
        List<Duty> duties = new ArrayList<>();
        LocalDate careerStartDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().minusYears(1);
        String careerContent = "상세 업무";

        assertThatThrownBy(() -> new Career(companyName, skills, duties, careerStartDate, endDate, careerContent))
                .isInstanceOf(CustomException.class);
    }

}
