package org.devcourse.resumeme.business.user.controller.mentee;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.user.controller.mentee.dto.MenteeInfoResponse;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.business.user.entity.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mentees")
public class MenteeController {

    private final UserService userService;

    @GetMapping("/{menteeId}")
    public MenteeInfoResponse getOne(@PathVariable Long menteeId) {
        User user = userService.getOne(menteeId);
        Mentee findMentee = Mentee.of(user);

        return new MenteeInfoResponse(findMentee);
    }

}
