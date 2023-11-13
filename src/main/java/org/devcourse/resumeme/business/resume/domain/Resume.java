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

import static org.devcourse.resumeme.common.util.Validator.notNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resume extends BaseEntity {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "resume_id")
    private Long id;

    @Getter
    private String title;

    @ManyToOne
    @JoinColumn(name = "mentee_id")
    private Mentee mentee;

    @Embedded
    private ResumeInfo resumeInfo;

    @Getter
    @Embedded
    private ReferenceLink referenceLink;

    private boolean openStatus;

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
    private Resume(Long id, String title, Mentee mentee, ResumeInfo resumeInfo, ReferenceLink referenceLink) {
        this.id = id;
        this.title = title;
        this.mentee = mentee;
        this.resumeInfo = resumeInfo;
        this.referenceLink = referenceLink;
    }

    public Resume copy() {
        return new Resume(
                null, title, mentee, resumeInfo, referenceLink
        );
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

    public void updateReferenceLink(ReferenceLink referenceLink) {
        this.referenceLink = referenceLink;
    }

}
