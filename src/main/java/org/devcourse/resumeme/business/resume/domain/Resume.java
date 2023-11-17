package org.devcourse.resumeme.business.resume.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.common.domain.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
    @ManyToOne
    @JoinColumn(name = "mentee_id")
    private Mentee mentee;

    @Embedded
    private ResumeInfo resumeInfo = new ResumeInfo();

    private boolean openStatus;

    private boolean deleted = false;

    @Getter
    private Long originResumeId;

    public Resume(String title, Mentee mentee) {
        validateResume(title, mentee);
        this.title = title;
        this.mentee = mentee;
        this.resumeInfo = new ResumeInfo();
    }

    private static void validateResume(String title, Mentee mentee) {
        notNull(title);
        notNull(mentee);
    }

    @Builder
    private Resume(Long id, String title, Mentee mentee, ResumeInfo resumeInfo) {
        this.id = id;
        this.title = title;
        this.mentee = mentee;
        this.resumeInfo = resumeInfo;
    }

    private Resume(Long id, String title, Mentee mentee, ResumeInfo resumeInfo, Long originResumeId) {
        this(id, title, mentee, resumeInfo);
        this.originResumeId = originResumeId;
    }

    public Resume copy() {
        return new Resume(null, title, mentee, resumeInfo, id);
    }

    public Long menteeId() {
        return this.mentee.getId();
    }

    public String menteeName() {
        return this.mentee.getRequiredInfo().getNickname();
    }

    public void openStatus() {
        this.openStatus = true;
    }

    public void updateResumeInfo(ResumeInfo resumeInfo) {
        this.resumeInfo = resumeInfo;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public ResumeInfo getResumeInfo() {
        if (resumeInfo == null) {
            resumeInfo = new ResumeInfo();
        }
        return resumeInfo;
    }

    public boolean isCopied() {
        return originResumeId != null;
    }

    public boolean isOrigin() {
        return originResumeId == null;
    }

}
