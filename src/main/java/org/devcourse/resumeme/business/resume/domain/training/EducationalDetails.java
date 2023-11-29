package org.devcourse.resumeme.business.resume.domain.training;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.devcourse.resumeme.common.util.Validator.notNull;

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
        notNull(organization);
        notNull(major);
        notNull(degree);
    }

}
