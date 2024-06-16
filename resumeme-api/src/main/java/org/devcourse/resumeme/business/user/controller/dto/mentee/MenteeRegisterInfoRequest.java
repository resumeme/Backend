package org.devcourse.resumeme.business.user.controller.dto.mentee;

import org.devcourse.resumeme.business.user.controller.dto.RequiredInfoRequest;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.service.vo.MenteeVo;
import org.devcourse.resumeme.business.user.service.vo.UserDomainVo;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;

import java.util.Set;

public record MenteeRegisterInfoRequest(String cacheKey, RequiredInfoRequest requiredInfo, Set<String> interestedPositions, Set<String> interestedFields, String introduce) {

    public UserDomainVo toVo(OAuth2TempInfo oAuth2TempInfo) {
        Mentee mentee = Mentee.builder()
                .email(oAuth2TempInfo.getEmail())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider().toUpperCase()))
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .requiredInfo(
                        new RequiredInfo(requiredInfo.realName(), requiredInfo.nickname(), requiredInfo.phoneNumber(), Role.of(requiredInfo.role()))
                )
                .interestedPositions(replaceNullWithEmptySet(interestedPositions))
                .interestedFields(replaceNullWithEmptySet(interestedFields))
                .introduce(introduce)
                .build();

        return new MenteeVo(mentee);
    }

    private Set<String> replaceNullWithEmptySet(Set<String> attributes) {
        if (attributes == null) {
            return Set.of();
        }

        return attributes;
    }

}
