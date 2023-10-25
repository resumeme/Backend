package org.devcourse.resumeme.domain.mentee;

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
import org.devcourse.resumeme.domain.user.Provider;

import java.util.HashSet;
import java.util.Set;

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
    private String oauthUsername;

    @Getter
    private String password;

    @Getter
    private String email;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String imageUrl;

    @Getter
    @Embedded
    private RequiredInfo requiredInfo;

    private String refreshToken;

    @OneToMany
    private Set<MenteePosition> interestedPositions = new HashSet<>();

    @OneToMany
    private Set<MenteeField> interestedFields = new HashSet<>();

    private String introduce;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Builder
    public Mentee(Long id, String oauthUsername, String password, String email, Provider provider, String imageUrl, RequiredInfo requiredInfo, String refreshToken, Set<MenteePosition> interestedPositions, Set<MenteeField> interestedFields, String introduce) {
        this.id = id;
        this.oauthUsername = oauthUsername;
        this.password = password;
        this.email = email;
        this.provider = provider;
        this.imageUrl = imageUrl;
        this.requiredInfo = requiredInfo;
        this.refreshToken = refreshToken;
        this.interestedPositions = interestedPositions;
        this.interestedFields = interestedFields;
        this.introduce = introduce;
    }

}
