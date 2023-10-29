package org.devcourse.resumeme.controller.dto;

import org.devcourse.resumeme.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.devcourse.resumeme.domain.user.Provider;
import org.devcourse.resumeme.global.auth.model.OAuth2TempInfo;

import java.util.Set;

public record MentorRegisterInfoRequest(String cacheKey, RequiredInfoRequest requiredInfo, Set<String> experiencedPositions, String careerContent, int careerYear, String introduce) {

    public Mentor toEntity(OAuth2TempInfo oAuth2TempInfo, String refreshToken) {
        return Mentor.builder()
                .email(oAuth2TempInfo.getEmail())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider().toUpperCase()))
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .requiredInfo(
                        new RequiredInfo(requiredInfo().realName(), requiredInfo().nickname(), requiredInfo().phoneNumber(), requiredInfo().role())
                )
                .refreshToken(refreshToken)
                .introduce(introduce)
                .experiencedPositions(experiencedPositions)
                .careerContent(careerContent)
                .careerYear(careerYear)
                .build();
    }
}
