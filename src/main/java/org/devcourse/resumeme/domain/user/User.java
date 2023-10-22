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

    private String username; // google_{고유번호}

    private String password;

    private String email; // 사용자 이메일

    @Enumerated(EnumType.STRING)
    private Role role; // ROLE_USER, ROLE_ADMIN

    @Enumerated(EnumType.STRING)
    private Provider provider; // google

    private String imageUrl;

    // 추가 필수 정보
    private String nickname;

    private String realName;

    private String phoneNumber;

    private String refreshToken;

    @Builder
    public User(Long id, String username, String password, String email, Role role, Provider provider, String imageUrl, String nickname, String realName, String phoneNumber, String refreshToken) {
        this.id = id;
        this.username = username;
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

}
