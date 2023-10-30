package org.devcourse.resumeme.domain.admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.validate;
import static org.devcourse.resumeme.global.advice.exception.ExceptionCode.NO_EMPTY_VALUE;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class MentorApplication {

    @Id
    @GeneratedValue
    @Column(name = "mentor_application_id")
    private Long id;

    @Getter
    private Long mentorId;

    public MentorApplication(Long mentorId) {
        validate(mentorId == null, NO_EMPTY_VALUE);

        this.mentorId = mentorId;
    }

}
