package org.devcourse.resumeme.domain.resume;

import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CertificationTest {

    @Test
    public void 제목이_비어있을_때_예외_발생() {
        assertThatThrownBy(() -> new Certification(null, "2023-01-01", "Issuing Authority", "Link", "Description"))
                .isInstanceOf(CustomException.class);
    }

}
