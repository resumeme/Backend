package org.devcourse.resumeme.domain.user;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdditionalInfo {

    @OneToMany(mappedBy = "user", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private Set<UserPosition> interestedPositions = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private Set<UserField> interestedFields = new HashSet<>();

    private String career;

    private String introduce;

    public AdditionalInfo(Set<UserPosition> interestedPositions, Set<UserField> interestedFields, String career, String introduce) {
        this.interestedPositions = interestedPositions;
        this.interestedFields = interestedFields;
        this.career = career;
        this.introduce = introduce;
    }

}
