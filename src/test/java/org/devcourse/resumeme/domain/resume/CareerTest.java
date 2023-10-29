package org.devcourse.resumeme.domain.resume;

import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CareerTest {

    private String companyName;
    private Position position;
    private List<String> skills;
    private List<Duty> duties;
    private boolean isCurrentlyEmployed;
    private String careerContent;

    @BeforeEach
    void init() {
        companyName = "Company";
        position = Position.BACK;
        skills = new ArrayList<>();
        duties = new ArrayList<>();
        isCurrentlyEmployed = true;
        careerContent = "상세 업무";
    }

    @Test
    void 기술_스택_및_업무_내용_없을_시_예외_발생() {
        LocalDate careerStartDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(2);

        assertThatThrownBy(() -> new Career(companyName, position, skills, duties, isCurrentlyEmployed, careerStartDate, endDate, careerContent))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 종료일이_시작일_보다_먼저일_시_예외_발생() {
        LocalDate careerStartDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().minusYears(1);

        assertThatThrownBy(() -> new Career(companyName, position, skills, duties, isCurrentlyEmployed, careerStartDate, endDate, careerContent))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 재직중일시_종료일_필수_입력() {
        LocalDate careerStartDate = LocalDate.now();
        LocalDate endDate = null;
        String careerContent = "상세 업무";

        assertThatThrownBy(() -> new Career(companyName, position, skills, duties, isCurrentlyEmployed, careerStartDate, endDate, careerContent))
                .isInstanceOf(CustomException.class);
    }

}
