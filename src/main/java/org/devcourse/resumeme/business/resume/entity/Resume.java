package org.devcourse.resumeme.business.resume.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.BaseEntity;
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.global.exception.ExceptionCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

import static org.devcourse.resumeme.common.util.Validator.Condition.isBlank;
import static org.devcourse.resumeme.common.util.Validator.notNull;

@Entity
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE resume SET deleted = true WHERE resume_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resume extends BaseEntity {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "resume_id")
    private Long id;

    @Getter
    private String title;

    @Getter
    private Long menteeId;

    @Embedded
    private ResumeInfo resumeInfo = new ResumeInfo();

    @Getter
    private Long originResumeId;

    @Getter
    private String memo;

    private boolean openStatus;

    private boolean deleted = false;

    public Resume(String title, Long menteeId) {
        validateResume(title, menteeId);
        this.title = title;
        this.menteeId = menteeId;
        this.resumeInfo = new ResumeInfo();
    }

    private static void validateResume(String title, Long menteeId) {
        Validator.check(isBlank(title.trim()), ExceptionCode.NO_EMPTY_VALUE);
        notNull(menteeId);
    }

    @Builder
    private Resume(Long id, String title, Long menteeId, ResumeInfo resumeInfo, Long originResumeId) {
        this.id = id;
        this.title = title;
        this.menteeId = menteeId;
        this.resumeInfo = resumeInfo;
        this.originResumeId = originResumeId;
    }

    public Resume copy() {
        return new Resume(null, title, menteeId, resumeInfo, id);
    }

    public void openStatus() {
        this.openStatus = true;
    }

    public void updateResumeInfo(ResumeInfo resumeInfo) {
        this.resumeInfo = resumeInfo;
    }

    public void updateTitle(String title) {
        Validator.check(isBlank(title.trim()), ExceptionCode.NO_EMPTY_VALUE);
        this.title = title;
    }

    public void updateMeme(String memo) {
        this.memo = memo;
    }

    public ResumeInfo getResumeInfo() {
        if (resumeInfo == null) {
            resumeInfo = new ResumeInfo();
        }
        return resumeInfo;
    }

    public boolean isOrigin() {
        return this.originResumeId == null;
    }

    public void makeToOrigin() {
        this.title = this.title.concat(" - 첨삭 반영 본").concat(LocalDate.now().toString());
        this.originResumeId = null;
    }

}
