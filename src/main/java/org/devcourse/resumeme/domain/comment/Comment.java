package org.devcourse.resumeme.domain.comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.BaseEntity;
import org.devcourse.resumeme.domain.resume.BlockType;
import org.devcourse.resumeme.domain.resume.Resume;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.Condition.isBlank;
import static org.devcourse.resumeme.common.util.Validator.check;
import static org.devcourse.resumeme.common.util.Validator.notNull;
import static org.devcourse.resumeme.global.advice.exception.ExceptionCode.NO_EMPTY_VALUE;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @Getter
    private String content;

    @Getter
    @Enumerated(STRING)
    private BlockType type;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    public Comment(String content, BlockType type, Resume resume) {
        check(isBlank(content), NO_EMPTY_VALUE);
        notNull(type);
        notNull(resume);

        this.content = content;
        this.type = type;
        this.resume = resume;
    }

}
