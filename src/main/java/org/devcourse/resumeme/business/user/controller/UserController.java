package org.devcourse.resumeme.business.user.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.user.controller.dto.UserInfoResponse;
import org.devcourse.resumeme.business.user.service.mentee.MenteeService;
import org.devcourse.resumeme.business.user.service.mentor.MentorService;
import org.devcourse.resumeme.global.auth.model.jwt.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final MentorService mentorService;

    private final MenteeService menteeService;

    @GetMapping
    public UserInfoResponse getMyInfo(@CurrentSecurityContext(expression = "authentication") Authentication auth) {
        JwtUser user = (JwtUser) auth.getPrincipal();

        if (isMentee(auth)) {
            return new UserInfoResponse(menteeService.getOne(user.id()));
        }

        return new UserInfoResponse(mentorService.getOne(user.id()));
    }

    private boolean isMentee(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_MENTEE"));
    }

}
