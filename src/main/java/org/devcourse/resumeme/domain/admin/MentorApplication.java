package org.devcourse.resumeme.domain.admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.domain.mentor.Mentor;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.validate;
import static org.devcourse.resumeme.global.advice.exception.ExceptionCode.NO_EMPTY_VALUE;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class MentorApplication {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "mentor_application_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    public MentorApplication(Mentor mentor) {
        validate(mentor == null, NO_EMPTY_VALUE);

        this.mentor = mentor;
    }

    public Long mentorId() {
        return mentor.getId();
    }

    public String mentorName() {
        return mentor.getRequiredInfo().getRealName();
    }

}
