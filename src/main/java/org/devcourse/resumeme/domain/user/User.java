package org.devcourse.resumeme.domain.user;

import jakarta.persistence.Column;
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

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

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

    private String nickname;

    private String realName;

    private String phoneNumber;

    private String refreshToken;

    @Builder
    public User(Long id, String oauthUsername, String password, String email, Role role, Provider provider, String imageUrl, String nickname, String realName, String phoneNumber, String refreshToken) {
        this.id = id;
        this.oauthUsername = oauthUsername;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.imageUrl = imageUrl;
        this.nickname = nickname;
        this.realName = realName;
        this.phoneNumber = phoneNumber;
        this.refreshToken = refreshToken;
    }

    public boolean isMentee() {
        if (this.role == Role.ROLE_MENTEE) {
            return true;
        }
        return false;
    }

}
