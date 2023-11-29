package org.devcourse.resumeme.business.resume.domain;

import org.devcourse.resumeme.business.resume.domain.language.ForeignLanguage;
import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.global.exception.CustomException;
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

    private Mentee mentee;

    @BeforeEach
    void init() {
        language = "영어";
        examName = "토익";
        scoreOrGrade = "100";
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
    void 외국어_생성에_성공한다() {
        ForeignLanguage foreignLanguage = new ForeignLanguage(language, examName, scoreOrGrade);

        assertThat(foreignLanguage).isNotNull();
    }


    @Test
    void 언어_누락_시_예외_발생() {
        assertThatThrownBy(() -> new ForeignLanguage(null, examName, scoreOrGrade))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 시험명_누락_시_예외_발생() {
        assertThatThrownBy(() -> new ForeignLanguage(language, null, scoreOrGrade))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 점수또는등급_누락_시_예외_발생() {
        assertThatThrownBy(() -> new ForeignLanguage(language, examName, null))
                .isInstanceOf(CustomException.class);
    }

}
