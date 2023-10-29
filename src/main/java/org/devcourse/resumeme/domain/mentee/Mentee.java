package org.devcourse.resumeme.domain.mentee;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.BaseEntity;
import org.devcourse.resumeme.common.domain.Field;
import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.domain.user.Provider;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mentee extends BaseEntity {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "mentee_id")
    private Long id;

    @Getter
    @Column(unique = true)
    private String email;

    @Getter
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Getter
    private String imageUrl;

    @Getter
    @Embedded
    private RequiredInfo requiredInfo;

    @Getter
    private String refreshToken;

    @Getter
    @OneToMany(fetch = LAZY, mappedBy = "mentee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<MenteePosition> interestedPositions = new HashSet<>();

    @Getter
    @OneToMany(fetch = LAZY, mappedBy = "mentee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<MenteeField> interestedFields = new HashSet<>();

    @Getter
    private String introduce;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Builder
    public Mentee(Long id,String email, Provider provider, String imageUrl, RequiredInfo requiredInfo, String refreshToken, Set<String> interestedPositions, Set<String> interestedFields, String introduce) {
        this.id = id;
        this.email = email;
        this.provider = provider;
        this.imageUrl = imageUrl;
        this.requiredInfo = requiredInfo;
        this.refreshToken = refreshToken;
        this.interestedPositions = interestedPositions.stream().map(position -> new MenteePosition(this, Position.valueOf(position.toUpperCase()))).collect(Collectors.toSet());
        this.interestedFields = interestedFields.stream().map(field -> new MenteeField(this, Field.valueOf(field.toUpperCase()))).collect(Collectors.toSet());
        this.introduce = introduce;
    }

}
