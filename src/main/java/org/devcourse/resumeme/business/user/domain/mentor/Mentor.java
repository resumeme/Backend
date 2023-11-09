package org.devcourse.resumeme.business.user.domain.mentor;

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
import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.business.user.controller.mentor.dto.MentorInfoUpdateRequest;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.global.exception.CustomException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.persistence.FetchType.LAZY;
import static org.devcourse.resumeme.business.user.domain.Role.*;
import static org.devcourse.resumeme.common.util.Validator.check;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NO_EMPTY_VALUE;
import static org.devcourse.resumeme.global.exception.ExceptionCode.ROLE_NOT_ALLOWED;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mentor extends BaseEntity {

    static final String EMAIL_REGEX = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "mentor_id")
    private Long id;

    @Getter
    @Column(unique = true)
    private String email;

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
    @OneToMany(fetch = LAZY, mappedBy = "mentor", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<MentorPosition> experiencedPositions = new HashSet<>();

    @Getter
    private String careerContent;

    @Getter
    private int careerYear;

    @Getter
    private String introduce;

    @Builder
    public Mentor(Long id, String email, Provider provider, String imageUrl, RequiredInfo requiredInfo, String refreshToken, Set<String> experiencedPositions, String careerContent, int careerYear, String introduce) {
        validateInputs(email, provider, imageUrl, requiredInfo, experiencedPositions, careerContent, careerYear, introduce);
        this.id = id;
        this.email = email;
        this.provider = provider;
        this.imageUrl = imageUrl;
        this.requiredInfo = requiredInfo;
        this.refreshToken = refreshToken;
        this.experiencedPositions = experiencedPositions.stream().map(position -> new MentorPosition(this, Position.valueOf(position.toUpperCase()))).collect(Collectors.toSet());
        this.careerContent = careerContent;
        this.careerYear = careerYear;
        this.introduce = introduce;
    }

    private void validateInputs(String email, Provider provider, String imageUrl, RequiredInfo requiredInfo, Set<String> experiencedPositions, String careerContent, int careerYear, String introduce) {
        check(email == null || email.isBlank() || !email.matches(EMAIL_REGEX), "INVALID_EMAIL", "이메일이 유효하지 않습니다.");
        check(provider == null, NO_EMPTY_VALUE);
        check(imageUrl == null || imageUrl.isBlank(), NO_EMPTY_VALUE);
        check(requiredInfo == null, NO_EMPTY_VALUE);
        check(ROLE_MENTEE.equals(requiredInfo.getRole()) || ROLE_ADMIN.equals(requiredInfo.getRole()), ROLE_NOT_ALLOWED);
        check(experiencedPositions == null || experiencedPositions.size() == 0, NO_EMPTY_VALUE);
        check(careerContent == null || careerContent.isBlank(), NO_EMPTY_VALUE);
        check(careerYear < 1 || careerYear > 80, "NUMBER_NOT_ALLOWED", "경력 연차가 올바르지 않습니다.");
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateRole(Role role) {
        if (ROLE_MENTEE.equals(role)) {
            throw new CustomException(ROLE_NOT_ALLOWED);
        }
        requiredInfo.updateRole(role);
    }

    public boolean isApproved() {
        return ROLE_MENTOR.equals(this.requiredInfo.getRole());
    }

    public void updateInfos(MentorInfoUpdateRequest updateRequest) {
        this.clearPositions();
        this.requiredInfo.updateRealName(updateRequest.realName());
        this.requiredInfo.updatePhoneNumber(updateRequest.phoneNumber());
        updateRequest.experiencedPositions().forEach(position -> this.experiencedPositions.add(new MentorPosition(this, Position.valueOf(position.toUpperCase()))));
        this.careerContent = updateRequest.careerContent();
        this.careerYear = updateRequest.careerYear();
        this.introduce = updateRequest.introduce();
    }

    public void clearPositions() {
        this.getExperiencedPositions().clear();
    }

}
