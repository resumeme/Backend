package org.devcourse.resumeme.business.user.controller.dto;

import org.devcourse.resumeme.business.user.controller.dto.mentee.MenteeRegisterInfoRequest;
import org.devcourse.resumeme.business.user.controller.dto.mentor.MentorRegisterInfoRequest;
import org.devcourse.resumeme.business.user.domain.Role;
import org.devcourse.resumeme.business.user.service.vo.UserDomainVo;
import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;
import org.devcourse.resumeme.global.exception.CustomException;

import static org.devcourse.resumeme.global.exception.ExceptionCode.BAD_REQUEST;

public record UserRegisterInfoRequest(
        MenteeRegisterInfoRequest mentees,
        MentorRegisterInfoRequest mentors
) {

    public UserDomainVo toVo(Role role, OAuth2TempInfo oAuth2TempInfo) {
        return switch (role) {
            case ROLE_MENTOR -> mentors.toVo(oAuth2TempInfo);
            case ROLE_MENTEE -> mentees.toVo(oAuth2TempInfo);
            case ROLE_PENDING, ROLE_ADMIN -> throw new CustomException(BAD_REQUEST);
        };
    }

    public String cacheKey() {
        if (mentees == null) {
            return mentors.cacheKey();
        }

        return mentees().cacheKey();
    }

}
