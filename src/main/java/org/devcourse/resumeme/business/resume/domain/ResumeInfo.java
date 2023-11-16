package org.devcourse.resumeme.business.resume.domain;

import jakarta.persistence.Embeddable;
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

    private String skills;

    @Lob
    @Getter
    private String introduce;

    public ResumeInfo(String position, List<String> skills, String introduce) {
        this.position = position;
        this.skills = String.join(",", skills);
        this.introduce = introduce;
    }

    public String getSkills() {
        if (this.skills == null) {
            return "";
        }

        return skills;
    }

}
