package org.devcourse.resumeme.business.resume.domain.training;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.exception.CustomException;
import org.devcourse.resumeme.global.exception.ExceptionCode;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GPADetails {

    @Getter
    private Double gpa;

    @Getter
    private Double maxGpa;

    public GPADetails(Double gpa, Double maxGpa) {
        if (maxGpa < gpa) {
            throw new CustomException(ExceptionCode.GPA_ERROR);
        }
        this.gpa = gpa;
        this.maxGpa = maxGpa;
    }

}
