package org.devcourse.resumeme.domain.review;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.BaseEntity;
import org.devcourse.resumeme.domain.resume.BlockType;
import org.devcourse.resumeme.domain.resume.Resume;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.Condition.isBlank;
import static org.devcourse.resumeme.common.util.Validator.validate;
import static org.devcourse.resumeme.global.advice.exception.ExceptionCode.NO_EMPTY_VALUE;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    private String content;

    @Enumerated(STRING)
    private BlockType type;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    public Review(String content, BlockType type, Resume resume) {
        validate(isBlank(content), NO_EMPTY_VALUE);
        validate(type == null, NO_EMPTY_VALUE);
        validate(resume == null, NO_EMPTY_VALUE);

        this.content = content;
        this.type = type;
        this.resume = resume;
    }

}
