package org.devcourse.resumeme.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.BaseEntity;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String oauthUsername;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String imageUrl;

    @Embedded
    private RequiredInfo requiredInfo;

    @Embedded
    private AdditionalInfo additionalInfo;

    private String refreshToken;

    @Builder
    public User(Long id, String oauthUsername, String password, String email, Role role, Provider provider, String imageUrl, RequiredInfo requiredInfo, String refreshToken, AdditionalInfo additionalInfo) {
        this.id = id;
        this.oauthUsername = oauthUsername;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.imageUrl = imageUrl;
        this.requiredInfo = requiredInfo;
        this.refreshToken = refreshToken;
        this.additionalInfo = additionalInfo;
    }

    public boolean isMentee() {
        return role.equals(Role.ROLE_MENTEE);
    }

}
