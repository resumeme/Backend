package org.devcourse.resumeme.business.user.controller.mentor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.user.controller.mentor.dto.MentorInfoResponse;
import org.devcourse.resumeme.business.user.controller.mentor.dto.MentorInfoUpdateRequest;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.business.user.entity.UserService;
import org.devcourse.resumeme.common.response.IdResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PatchMapping("/{mentorId}")
    public IdResponse update(@PathVariable Long mentorId, @RequestBody MentorInfoUpdateRequest mentorInfoUpdateRequest) {
        Long updatedMentorId = userService.update(mentorId, mentorInfoUpdateRequest);

        return new IdResponse(updatedMentorId);
    }

}
