package org.devcourse.resumeme.domain.resume;

import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ForeignLanguageTest {

    private String language;
    private String examName;
    private String scoreOrGrade;

    private Resume resume;

    @BeforeEach
    void init() {
        language = "영어";
        examName = "토익";
        scoreOrGrade = "100";
        resume = new Resume("title", Mentee.builder()
                .interestedPositions(Set.of("BACK"))
                .interestedFields(Set.of("FINANCE"))
                .build());
    }

    @Test
    void 외국어_생성에_성공한다() {
        ForeignLanguage foreignLanguage = new ForeignLanguage(language, examName, scoreOrGrade, resume);

        assertThat(foreignLanguage).isNotNull();
    }


    @Test
    void 언어_누락_시_예외_발생() {
        assertThatThrownBy(() -> new ForeignLanguage(null, examName, scoreOrGrade, resume))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 시험명_누락_시_예외_발생() {
        assertThatThrownBy(() -> new ForeignLanguage(language, null, scoreOrGrade, resume))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 점수또는등급_누락_시_예외_발생() {
        assertThatThrownBy(() -> new ForeignLanguage(language, examName, null, resume))
                .isInstanceOf(CustomException.class);
    }

}
