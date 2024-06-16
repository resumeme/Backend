package org.devcourse.resumeme.business.resume.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeInfo {

    @Getter
    private String position;

    private String skills;

    @Lob
    @Getter
    private String introduce;

    public ResumeInfo(String position, List<String> skills, String introduce) {
        this.position = position;
        this.skills = String.join(",", skills);
        this.introduce = introduce;
    }

    public Set<String> getSkillSet() {
        if (this.skills == null) {
            return Set.of();
        }

        return Set.of(skills.split(","));
    }

}
