package org.devcourse.resumeme.business.resume.domain;

import org.devcourse.resumeme.business.resume.domain.certification.Certification;
import org.devcourse.resumeme.global.exception.CustomException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CertificationTest {

    @Test
    void 수상_및_자격증_생성에_성공한다() {
        Certification certification = new Certification("제목", "2023-01-01", "수여 기관", "Link", "Description");
        assertThat(certification).isNotNull();
    }

    @Test
    void 제목이_비어있을_때_예외_발생() {
        assertThatThrownBy(() -> new Certification(null, "2023-01-01", "수여 기관", "Link", "Description"))
                .isInstanceOf(CustomException.class);
    }

}
