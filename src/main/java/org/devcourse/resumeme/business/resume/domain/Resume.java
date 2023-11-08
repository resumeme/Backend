package org.devcourse.resumeme.business.resume.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.common.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
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
    @OneToMany(mappedBy = "resume", cascade = {PERSIST, REMOVE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Project> project = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "resume", cascade = {PERSIST, REMOVE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Training> training = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "resume", cascade = {PERSIST, REMOVE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Certification> certification = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "resume", cascade = {PERSIST, REMOVE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Activity> activity = new ArrayList<>();

    @Getter
    @OneToMany(mappedBy = "resume", cascade = {PERSIST, REMOVE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ForeignLanguage> foreignLanguage = new ArrayList<>();

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
    private Resume(Long id, String title, Mentee mentee, ResumeInfo resumeInfo,
                   List<Project> project, List<Training> training, List<Certification> certification, List<Activity> activity,
                   List<ForeignLanguage> foreignLanguage, ReferenceLink referenceLink) {
        this.id = id;
        this.title = title;
        this.mentee = mentee;
        this.resumeInfo = resumeInfo;
        this.project = project;
        this.training = training;
        this.certification = certification;
        this.activity = activity;
        this.foreignLanguage = foreignLanguage;
        this.referenceLink = referenceLink;
        this.openStatus = false;
    }

    public Resume copy() {
        return new Resume(
                null, title, mentee, resumeInfo, project, training, certification, activity, foreignLanguage,
                 referenceLink
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
