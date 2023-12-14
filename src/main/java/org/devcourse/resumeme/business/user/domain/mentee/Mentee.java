package org.devcourse.resumeme.business.user.domain.mentee;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.entity.RequiredInfoEntity;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.common.domain.BaseEntity;
import org.devcourse.resumeme.common.domain.Field;
import org.devcourse.resumeme.common.domain.Position;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.devcourse.resumeme.common.util.Validator.check;
import static org.devcourse.resumeme.global.exception.ExceptionCode.INVALID_EMAIL;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NO_EMPTY_VALUE;
import static org.devcourse.resumeme.global.exception.ExceptionCode.ROLE_NOT_ALLOWED;
import static org.devcourse.resumeme.global.exception.ExceptionCode.TEXT_OVER_LENGTH;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mentee extends BaseEntity {

    static final String EMAIL_REGEX = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";

    private Long id;

    private String email;

    private Provider provider;

    private String imageUrl;

    private RequiredInfo requiredInfo;

    private String refreshToken;

    private Set<Position> interestedPositions = new HashSet<>();

    private Set<Field> interestedFields = new HashSet<>();

    private String introduce;

    @Builder
    public Mentee(Long id,String email, Provider provider, String imageUrl, RequiredInfo requiredInfo, String refreshToken, Set<String> interestedPositions, Set<String> interestedFields, String introduce) {
        validateInputs(email, provider, imageUrl, requiredInfo, introduce);
        this.id = id;
        this.email = email;
        this.provider = provider;
        this.imageUrl = imageUrl;
        this.requiredInfo = requiredInfo;
        this.refreshToken = refreshToken;
        this.interestedPositions = interestedPositions.stream().map(Position::valueOf).collect(toSet());
        this.interestedFields = interestedFields.stream().map(Field::valueOf).collect(toSet());
        this.introduce = introduce;
    }

    private void validateInputs(String email, Provider provider, String imageUrl, RequiredInfo requiredInfo, String introduce) {
        check(email == null || email.isBlank() || !email.matches(EMAIL_REGEX), INVALID_EMAIL);
        check(provider == null, NO_EMPTY_VALUE);
        check(imageUrl == null || imageUrl.isBlank(), NO_EMPTY_VALUE);
        check(requiredInfo == null, NO_EMPTY_VALUE);
        check(!Role.ROLE_MENTEE.equals(requiredInfo.getRole()), ROLE_NOT_ALLOWED);
        check(introduce != null && introduce.length() > 100, TEXT_OVER_LENGTH);
    }

    public void updateBasicInfo(String nickname, String phoneNumber, String introduce) {
        check(introduce != null && introduce.length() > 100, TEXT_OVER_LENGTH);
        this.requiredInfo.update(nickname, phoneNumber);
        this.introduce = introduce;
    }

    public void updatePositionAndFields(Set<String> interestedPositions, Set<String> interestedFields) {
        this.clearPositions();
        this.clearFields();
        interestedPositions.forEach(position -> this.interestedPositions.add(Position.valueOf(position.toUpperCase())));
        interestedFields.forEach(field -> this.interestedFields.add(Field.valueOf(field.toUpperCase())));
    }

    public void clearPositions() {
        this.getInterestedPositions().clear();
    }

    public void clearFields() {
        this.getInterestedFields().clear();
    }

    public String getRoleName() {
        return requiredInfo.getRole().getRoleName();
    }

    public static Mentee of(User user) {
        return Mentee.builder()
                .id(user.getId())
                .email(user.getEmail())
                .provider(user.getProvider())
                .imageUrl(user.getImageUrl())
                .requiredInfo(RequiredInfo.of(user))
                .refreshToken(user.getRefreshToken())
                .interestedPositions(user.getUserPositions().stream().map(position -> position.getPosition().name()).collect(toSet()))
                .interestedFields(user.getInterestedFields().stream().map(field -> field.getField().name()).collect(toSet()))
                .introduce(user.getIntroduce())
                .build();
    }

    public User from() {
        return User.builder()
                .id(id)
                .email(email)
                .provider(provider)
                .imageUrl(imageUrl)
                .requiredInfo(new RequiredInfoEntity(requiredInfo))
                .refreshToken(refreshToken)
                .userPositions(interestedPositions.stream().map(Enum::name).collect(toSet()))
                .interestedFields(interestedFields.stream().map(Enum::name).collect(toSet()))
                .introduce(introduce)
                .build();
    }

}
