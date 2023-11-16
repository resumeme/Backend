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

    @Getter
    @Embedded
    private ResumeInfo resumeInfo;

    private boolean openStatus;

    private boolean deleted = false;

    public Resume(String title, Mentee mentee) {
        validateResume(title, mentee);
        this.title = title;
        this.mentee = mentee;
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

    public Resume copy() {
        return new Resume(null, title, mentee, resumeInfo);
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

}
