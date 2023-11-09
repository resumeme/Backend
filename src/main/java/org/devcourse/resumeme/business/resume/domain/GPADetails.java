package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.exception.CustomException;
import org.devcourse.resumeme.global.exception.ExceptionCode;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GPADetails {

    @Getter
    private double gpa;

    @Getter
    private double maxGpa;

    public GPADetails(double gpa, double maxGpa) {
        if (maxGpa < gpa) {
            throw new CustomException(ExceptionCode.GPA_ERROR);
        }
        this.gpa = gpa;
        this.maxGpa = maxGpa;
    }

}
