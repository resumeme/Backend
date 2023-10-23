package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

import static org.devcourse.resumeme.common.util.Validator.validate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Resume {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "resume_id")
    private Long id;

    private String title;

    private boolean isWriting;

    private boolean isRepresentative;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Lob
    private String introduce;

    @Embedded
    private Training training;

    @Embedded
    private Career career;

    @Embedded
    private Project project;

    private String email;

    private String githubAddress;

    private String blogAddress;

    private String phoneNumber;

    public Resume(String title, String introduce) {
        validateInput(title, introduce);

        this.title = title;
        this.introduce = introduce;
    }

    private void validateInput(String title, String introduce) {
        validate(title == null, ExceptionCode.NO_EMPTY_VALUE);
        validate(introduce == null, ExceptionCode.NO_EMPTY_VALUE);
    }

}
