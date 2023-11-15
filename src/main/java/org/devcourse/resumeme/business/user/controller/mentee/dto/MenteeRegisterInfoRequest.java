package org.devcourse.resumeme.business.user.controller.mentee.dto;

import org.devcourse.resumeme.business.user.controller.dto.RequiredInfoRequest;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;

import java.util.Set;

public record MenteeRegisterInfoRequest(String cacheKey, RequiredInfoRequest requiredInfo, Set<String> interestedPositions, Set<String> interestedFields, String introduce) {

    public Mentee toEntity(OAuth2TempInfo oAuth2TempInfo) {
        return Mentee.builder()
                .email(oAuth2TempInfo.getEmail())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider().toUpperCase()))
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .requiredInfo(
                        new RequiredInfo(requiredInfo.realName(), requiredInfo.nickname(), requiredInfo.phoneNumber(), requiredInfo.role())
                )
                .interestedPositions(replaceNullWithEmptySet(interestedPositions))
                .interestedFields(replaceNullWithEmptySet(interestedFields))
                .introduce(introduce)
                .build();
    }

    private Set<String> replaceNullWithEmptySet(Set<String> setAttributes) {
        return setAttributes == null ? Set.of() : setAttributes;
    }

}
