package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.BaseEntity;
import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static org.devcourse.resumeme.common.util.Validator.check;

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

    @Enumerated(EnumType.STRING)
    private Position position;

    @Lob
    private String introduce;

    @Getter
    @OneToMany(mappedBy = "resume", cascade = {PERSIST, REMOVE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Career> career = new ArrayList<>();

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

    private String email;

    private String githubAddress;

    private String blogAddress;

    private String phoneNumber;

    private boolean openStatus;

    public Resume(String title, Mentee mentee) {
        validateResume(title, mentee);
        this.title = title;
        this.mentee = mentee;
    }

    private static void validateResume(String title, Mentee mentee) {
        Validator.check(title == null, ExceptionCode.NO_EMPTY_VALUE);
        Validator.check(mentee == null, ExceptionCode.MENTEE_NOT_FOUND);
    }

    @Builder
    private Resume(Long id, String title, Mentee mentee, Position position, String introduce, List<Career> career,
                   List<Project> project, List<Training> training, List<Certification> certification, List<Activity> activity,
                   List<ForeignLanguage> foreignLanguage, String email, String githubAddress, String blogAddress, String phoneNumber) {
        this.id = id;
        this.title = title;
        this.mentee = mentee;
        this.position = position;
        this.introduce = introduce;
        this.career = career;
        this.project = project;
        this.training = training;
        this.certification = certification;
        this.activity = activity;
        this.foreignLanguage = foreignLanguage;
        this.email = email;
        this.githubAddress = githubAddress;
        this.blogAddress = blogAddress;
        this.phoneNumber = phoneNumber;
        this.openStatus = false;
    }

    public Resume copy() {
        return new Resume(
                null, title, mentee, position, introduce,
                career, project, training, certification, activity, foreignLanguage,
                email, githubAddress, blogAddress, phoneNumber
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

}
