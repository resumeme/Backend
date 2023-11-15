package org.devcourse.resumeme.business.resume.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeInfo {

    @Getter
    private String position;

    @Getter
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "resume_skills")
    @Column(name = "skill")
    private List<String> skills;

    @Lob
    @Getter
    private String introduce;

    public ResumeInfo(String position, List<String> skills, String introduce) {
        this.position = position;
        this.skills = skills;
        this.introduce = introduce;
    }

}
