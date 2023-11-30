package org.devcourse.resumeme.business.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.common.domain.Field;
import org.devcourse.resumeme.common.domain.Position;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
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
    private RequiredInfoEntity requiredInfo;

    @Getter
    private String careerContent;

    @Getter
    private int careerYear;

    @Getter
    private String refreshToken;

    @OneToMany(mappedBy = "user", cascade = {PERSIST, MERGE, REMOVE}, orphanRemoval = true)
    private Set<UserPosition> userPositions = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = {PERSIST, MERGE, REMOVE}, orphanRemoval = true)
    private Set<InterestedField> interestedFields = new HashSet<>();

    @Getter
    private String introduce;

    @Builder
    public User(Long id, String email, Provider provider, String imageUrl, RequiredInfoEntity requiredInfo, String careerContent, int careerYear, String refreshToken, String introduce, Set<String> userPositions, Set<String> interestedFields) {
        this.id = id;
        this.email = email;
        this.provider = provider;
        this.imageUrl = imageUrl;
        this.requiredInfo = requiredInfo;
        this.careerContent = careerContent;
        this.careerYear = careerYear;
        this.refreshToken = refreshToken;
        this.introduce = introduce;
        this.userPositions.clear();
        this.userPositions = userPositions.stream()
                .map(position -> new UserPosition(this, Position.valueOf(position.toUpperCase())))
                .collect(toSet());
        this.interestedFields.clear();
        this.interestedFields = interestedFields.stream()
                .map(field -> new InterestedField(this, Field.valueOf(field.toUpperCase())))
                .collect(toSet());
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
