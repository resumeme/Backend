package org.devcourse.resumeme.business.result.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Resume;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class ResultNotice {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "result_notice_id")
    private Long id;

    @Lob
    private String content;

    @Getter
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    public ResultNotice(String content, Resume resume) {
        this.content = content;
        this.resume = resume;
        resume.openStatus();
    }

}
