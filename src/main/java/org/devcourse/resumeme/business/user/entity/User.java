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
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.service.vo.UserInfoUpdateVo;
import org.devcourse.resumeme.common.domain.Field;
import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.global.exception.CustomException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.business.user.domain.Role.ROLE_MENTEE;
import static org.devcourse.resumeme.common.util.Validator.check;
import static org.devcourse.resumeme.global.exception.ExceptionCode.ROLE_NOT_ALLOWED;
import static org.devcourse.resumeme.global.exception.ExceptionCode.TEXT_OVER_LENGTH;

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
    private RequiredInfo requiredInfo;

    @Getter
    private String careerContent;

    @Getter
    private int careerYear;

    @Getter
    private String refreshToken;

    @OneToMany(mappedBy = "user", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private Set<UserPosition> userPositions = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    private Set<InterestedField> interestedFields = new HashSet<>();

    @Getter
    private String introduce;

    @Builder
    public User(Long id, String email, Provider provider, String imageUrl, RequiredInfo requiredInfo, String careerContent, int careerYear, Set<String> userPositions, Set<String> interestedFields) {
        this.id = id;
        this.email = email;
        this.provider = provider;
        this.imageUrl = imageUrl;
        this.requiredInfo = requiredInfo;
        this.careerContent = careerContent;
        this.careerYear = careerYear;
        this.userPositions = userPositions.stream()
                .map(position -> new UserPosition(this, Position.valueOf(position.toUpperCase())))
                .collect(Collectors.toSet());
        this.interestedFields = interestedFields.stream()
                .map(field -> new InterestedField(this, Field.valueOf(field.toUpperCase())))
                .collect(Collectors.toSet());
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateInfos(UserInfoUpdateVo updateRequest) {
        clearPositions();
        clearFields();
        this.requiredInfo.updateNickname(updateRequest.nickname());
        this.requiredInfo.updatePhoneNumber(updateRequest.phoneNumber());
        updateRequest.interestedPositions().forEach(position -> this.userPositions.add(new UserPosition(this, Position.valueOf(position.toUpperCase()))));
        updateRequest.interestedFields().forEach(field -> this.interestedFields.add(new InterestedField(this, Field.valueOf(field.toUpperCase()))));
        updateIntroduce(updateRequest.introduce());
    }

    private void clearPositions() {
        userPositions.clear();
    }

    private void clearFields() {
        interestedFields.clear();
    }

    private void updateIntroduce(String introduce) {
        check(introduce != null && introduce.length() > 100, TEXT_OVER_LENGTH);
        this.introduce = introduce;
    }

    public void updateRole(Role role) {
        if (ROLE_MENTEE.equals(role)) {
            throw new CustomException(ROLE_NOT_ALLOWED);
        }
        requiredInfo.updateRole(role);
    }

}
