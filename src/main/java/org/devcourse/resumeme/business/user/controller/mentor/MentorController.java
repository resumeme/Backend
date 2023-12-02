package org.devcourse.resumeme.business.user.controller.mentor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.user.controller.mentor.dto.MentorInfoResponse;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.business.user.entity.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final UserService userService;

    @GetMapping("/{mentorId}")
    public MentorInfoResponse getOne(@PathVariable Long mentorId) {
        User user = userService.getOne(mentorId);
        Mentor findMentor = Mentor.of(user);

        return new MentorInfoResponse(findMentor);
    }

}
