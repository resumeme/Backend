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
import org.devcourse.resumeme.controller.dto.MenteeInfoUpdateRequest;
import org.devcourse.resumeme.domain.user.Provider;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.persistence.FetchType.LAZY;
import static org.devcourse.resumeme.common.util.Validator.check;
import static org.devcourse.resumeme.domain.user.Role.ROLE_MENTEE;
import static org.devcourse.resumeme.global.advice.exception.ExceptionCode.NO_EMPTY_VALUE;
import static org.devcourse.resumeme.global.advice.exception.ExceptionCode.ROLE_NOT_ALLOWED;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mentee extends BaseEntity {

    static final String EMAIL_REGEX = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";

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
    @OneToMany(fetch = LAZY, mappedBy = "mentee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<MenteePosition> interestedPositions = new HashSet<>();

    @Getter
    @OneToMany(fetch = LAZY, mappedBy = "mentee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<MenteeField> interestedFields = new HashSet<>();

    @Getter
    private String introduce;

    @Builder
    public Mentee(Long id,String email, Provider provider, String imageUrl, RequiredInfo requiredInfo, String refreshToken, Set<String> interestedPositions, Set<String> interestedFields, String introduce) {
        validateInputs(email, provider, imageUrl, requiredInfo, refreshToken);
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

    private void validateInputs(String email, Provider provider, String imageUrl, RequiredInfo requiredInfo, String refreshToken) {
        check(email == null || email.isBlank() || !email.matches(EMAIL_REGEX), "INVALID_EMAIL", "이메일이 유효하지 않습니다.");
        check(provider == null, NO_EMPTY_VALUE);
        check(imageUrl == null || imageUrl.isBlank(), NO_EMPTY_VALUE);
        check(requiredInfo == null, NO_EMPTY_VALUE);
        check(!ROLE_MENTEE.equals(requiredInfo.getRole()), ROLE_NOT_ALLOWED);
        check(refreshToken == null || refreshToken.isBlank(), NO_EMPTY_VALUE);
    }

    public void updateInfos(MenteeInfoUpdateRequest updateRequest) {
        this.clearPositions();
        this.clearFields();
        this.requiredInfo.updateNickname(updateRequest.nickname());
        this.requiredInfo.updatePhoneNumber(updateRequest.phoneNumber());
        updateRequest.interestedPositions().forEach(position -> this.interestedPositions.add(new MenteePosition(this, Position.valueOf(position.toUpperCase()))));
        updateRequest.interestedFields().forEach(field -> this.interestedFields.add(new MenteeField(this, Field.valueOf(field.toUpperCase()))));
        this.introduce = updateRequest.introduce();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void clearPositions() {
        this.getInterestedPositions().clear();
    }

    public void clearFields() {
        this.getInterestedFields().clear();
    }

}
