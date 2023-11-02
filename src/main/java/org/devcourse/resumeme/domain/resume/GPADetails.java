package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GPADetails {

    @Getter
    private double gpa;

    @Getter
    private double maxGpa;

    public GPADetails(double gpa, double maxGpa) {
        if (maxGpa <= gpa) {
            throw new CustomException(ExceptionCode.GPA_ERROR);
        }
        this.gpa = gpa;
        this.maxGpa = maxGpa;
    }

}
