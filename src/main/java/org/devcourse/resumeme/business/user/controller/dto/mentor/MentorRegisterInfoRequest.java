package org.devcourse.resumeme.business.user.controller.dto.mentor;

import org.devcourse.resumeme.business.user.controller.dto.RequiredInfoRequest;
import org.devcourse.resumeme.business.user.domain.Provider;
import org.devcourse.resumeme.business.user.domain.mentee.RequiredInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.service.vo.MentorVo;
import org.devcourse.resumeme.business.user.service.vo.UserDomainVo;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;

import java.util.Set;

import static org.devcourse.resumeme.business.user.domain.Role.ROLE_PENDING;

public record MentorRegisterInfoRequest(String cacheKey, RequiredInfoRequest requiredInfo, Set<String> experiencedPositions, String careerContent, int careerYear, String introduce) {

    public UserDomainVo toVo(OAuth2TempInfo oAuth2TempInfo) {
        Mentor mentor = Mentor.builder()
                .email(oAuth2TempInfo.getEmail())
                .provider(Provider.valueOf(oAuth2TempInfo.getProvider().toUpperCase()))
                .imageUrl(oAuth2TempInfo.getImageUrl())
                .requiredInfo(
                        new RequiredInfo(requiredInfo().realName(), requiredInfo().nickname(), requiredInfo().phoneNumber(), ROLE_PENDING)
                )
                .introduce(introduce)
                .experiencedPositions(experiencedPositions)
                .careerContent(careerContent)
                .careerYear(careerYear)
                .build();

        return new MentorVo(mentor);
    }
}
