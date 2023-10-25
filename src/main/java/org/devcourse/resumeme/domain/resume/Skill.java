package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Skill {

    @Getter
    private String technology;

    public Skill(String technology) {
        this.technology = technology;
    }

}
