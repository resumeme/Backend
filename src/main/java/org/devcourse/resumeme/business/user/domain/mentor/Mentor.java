package org.devcourse.resumeme.business.user.domain.mentor;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.user.controller.mentor.dto.MentorInfoUpdateRequest;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.common.domain.BaseEntity;
import org.devcourse.resumeme.common.domain.Position;
import org.devcourse.resumeme.global.exception.CustomException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.devcourse.resumeme.business.user.domain.Role.ROLE_ADMIN;
import static org.devcourse.resumeme.business.user.domain.Role.ROLE_MENTEE;
import static org.devcourse.resumeme.business.user.domain.Role.ROLE_MENTOR;
import static org.devcourse.resumeme.common.util.Validator.check;
import static org.devcourse.resumeme.global.exception.ExceptionCode.CAREERYEAR_NOT_ALLOWED;
import static org.devcourse.resumeme.global.exception.ExceptionCode.INVALID_EMAIL;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NO_EMPTY_VALUE;
import static org.devcourse.resumeme.global.exception.ExceptionCode.ROLE_NOT_ALLOWED;
import static org.devcourse.resumeme.global.exception.ExceptionCode.TEXT_OVER_LENGTH;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mentor extends BaseEntity {

    static final String EMAIL_REGEX = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";

    @Getter
    private Long id;

    @Getter
    private String email;

    @Getter
    private Provider provider;

    @Getter
    private String imageUrl;

    @Getter
    private RequiredInfo requiredInfo;

    @Getter
    private String refreshToken;

    @Getter
    private Set<Position> experiencedPositions = new HashSet<>();

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
        this.experiencedPositions = experiencedPositions.stream().map(position -> Position.valueOf(position.toUpperCase())).collect(Collectors.toSet());
        this.careerContent = careerContent;
        this.careerYear = careerYear;
        this.introduce = introduce;
    }

    private void validateInputs(String email, Provider provider, String imageUrl, RequiredInfo requiredInfo, Set<String> experiencedPositions, String careerContent, int careerYear, String introduce) {
        check(email == null || email.isBlank() || !email.matches(EMAIL_REGEX), INVALID_EMAIL);
        check(provider == null, NO_EMPTY_VALUE);
        check(imageUrl == null || imageUrl.isBlank(), NO_EMPTY_VALUE);
        check(requiredInfo == null, NO_EMPTY_VALUE);
        check(ROLE_MENTEE.equals(requiredInfo.getRole()) || ROLE_ADMIN.equals(requiredInfo.getRole()), ROLE_NOT_ALLOWED);
        check(experiencedPositions == null || experiencedPositions.size() == 0, NO_EMPTY_VALUE);
        check(careerContent == null || careerContent.isBlank(), NO_EMPTY_VALUE);
        check(careerContent.length() > 300, TEXT_OVER_LENGTH);
        check(careerYear < 1 || careerYear > 80, CAREERYEAR_NOT_ALLOWED);
        check(introduce != null && introduce.length() > 100, TEXT_OVER_LENGTH);
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
        validateInputs(email, provider, imageUrl, requiredInfo,updateRequest.experiencedPositions(), updateRequest.careerContent(), updateRequest.careerYear(), updateRequest.introduce());
        this.clearPositions();
        this.requiredInfo.updateNickname(updateRequest.nickname());
        this.requiredInfo.updatePhoneNumber(updateRequest.phoneNumber());
        updateRequest.experiencedPositions().forEach(position -> this.experiencedPositions.add(Position.valueOf(position.toUpperCase())));
        this.careerContent = updateRequest.careerContent();
        this.careerYear = updateRequest.careerYear();
        this.introduce = updateRequest.introduce();
    }

    public void clearPositions() {
        this.getExperiencedPositions().clear();
    }

    public String getRoleName() {
        return requiredInfo.getRole().getRoleName();
    }

    public Role getRole() {
        return requiredInfo.getRole();
    }

    public String getNickname() {
        return requiredInfo.getNickname();
    }

}
