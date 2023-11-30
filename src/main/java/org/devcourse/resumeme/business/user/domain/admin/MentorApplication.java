package org.devcourse.resumeme.business.user.domain.admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.notNull;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class MentorApplication {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "mentor_application_id")
    private Long id;

    @Getter
    private Long mentorId;

    public MentorApplication(Long mentorId) {
        notNull(mentorId);

        this.mentorId = mentorId;
    }

}
