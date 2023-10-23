package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.controller.dto.PrimarySignUpInfo;
import org.devcourse.resumeme.domain.user.User;
import org.devcourse.resumeme.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Long> register(@RequestBody PrimarySignUpInfo primarySignUpInfo) {
        User findUser = userService.getOne(primarySignUpInfo.id());

        // 필수 추가 정보까지 입력한 경우 -> 토큰 발급 후 전달 로직 추가 필요
        User userWithPrimaryInfo = User.builder()
                .id(findUser.getId())
                .oauthUsername(findUser.getOauthUsername())
                .email(findUser.getEmail())
                .password(findUser.getPassword())
                .provider(findUser.getProvider())
                .role(primarySignUpInfo.role())
                .realName(primarySignUpInfo.realName())
                .phoneNumber(primarySignUpInfo.phoneNumber()).build();

        Long savedUserId = userService.completeSignUp(userWithPrimaryInfo);

        return new ResponseEntity<>(savedUserId, HttpStatus.OK);
    }

}
