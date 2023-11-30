package org.devcourse.resumeme.business.user.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.user.controller.dto.UserInfoResponse;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.business.user.entity.UserService;
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

    private final UserService userService;

    @GetMapping
    public UserInfoResponse getMyInfo(@CurrentSecurityContext(expression = "authentication") Authentication auth) {
        JwtUser jwtUser = (JwtUser) auth.getPrincipal();
        User user = userService.getOne(jwtUser.id());
        if (isMentee(auth)) {
            return new UserInfoResponse(Mentee.of(user));
        }

        return new UserInfoResponse(Mentor.of(user));
    }

    private boolean isMentee(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_MENTEE"));
    }

}
