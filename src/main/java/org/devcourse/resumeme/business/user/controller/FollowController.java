package org.devcourse.resumeme.business.user.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.user.controller.dto.FollowRequest;
import org.devcourse.resumeme.business.user.controller.dto.FollowResponse;
import org.devcourse.resumeme.business.user.domain.mentee.Follow;
import org.devcourse.resumeme.business.user.service.mentee.FollowService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.devcourse.resumeme.global.auth.model.jwt.JwtUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @GetMapping
    public List<FollowResponse> getFollowList(@AuthenticationPrincipal JwtUser user) {
        return followService.getFollowings(user.id())
                .stream()
                .map(FollowResponse::new)
                .toList();
    }

    @PostMapping
    public IdResponse doFollow(@RequestBody FollowRequest request, @AuthenticationPrincipal JwtUser user) {
        Follow follow = new Follow(user.id(), request.mentorId());
        Long followId = followService.create(follow);

        return new IdResponse(followId);
    }

    @DeleteMapping("/{followId}")
    public void unFollow(@PathVariable Long followId) {
        followService.unfollow(followId);
    }

}
