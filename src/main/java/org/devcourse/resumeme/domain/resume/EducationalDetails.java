package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

import static org.devcourse.resumeme.common.util.Validator.notNull;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EducationalDetails {

    @Getter
    private String organization;

    @Getter
    private String major;

    @Getter
    private String degree;

    public EducationalDetails(String organization, String major, String degree) {
        validateEducationalDetails(organization, major, degree);
        this.organization = organization;
        this.major = major;
        this.degree = degree;
    }

    private static void validateEducationalDetails(String organization, String major, String degree) {
        notNull(ExceptionCode.NO_EMPTY_VALUE);
        notNull(ExceptionCode.NO_EMPTY_VALUE);
        notNull(ExceptionCode.NO_EMPTY_VALUE);
    }

}
