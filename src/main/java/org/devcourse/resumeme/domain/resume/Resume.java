package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.BaseEntity;
import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.domain.user.User;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

import static org.devcourse.resumeme.common.util.Validator.validate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resume extends BaseEntity {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "resume_id")
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Lob
    private String introduce;

    @ManyToOne
    @JoinColumn(name = "training_id")
    private Training training;

    @Embedded
    private Career career;

    @Embedded
    private Project project;

    private String email;

    private String githubAddress;

    private String blogAddress;

    private String phoneNumber;

    public Resume(String title, User user, Training training) {
        validateResume(title, user, training);
        this.title = title;
        this.user = user;
        this.training = training;
    }

    private static void validateResume(String title, User user, Training training) {
        validate(title == null, ExceptionCode.NO_EMPTY_VALUE);
        validate(user == null, ExceptionCode.MENTEE_NOT_FOUND);
        validate(training == null, ExceptionCode.NO_EMPTY_VALUE);
        if (!user.isMentee()) {
            throw new CustomException(ExceptionCode.MENTEE_ONLY_RESUME);
        }
    }

    public Resume(String title, String introduce) {
        this.title = title;
        this.introduce = introduce;
    }

}
