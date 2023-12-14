package org.devcourse.resumeme.business.userevent.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.event.service.MenteeToEventService;
import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.business.user.service.UserProvider;
import org.devcourse.resumeme.business.user.service.vo.UserResponse;
import org.devcourse.resumeme.business.userevent.controller.dto.MenteeEventResponse;
import org.devcourse.resumeme.business.userevent.controller.dto.MentorEventResponse;
import org.devcourse.resumeme.business.userevent.controller.dto.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserEventController {

    private final ResumeService resumeService;

    private final UserProvider userProvider;

    private final MenteeToEventService menteeToEventService;

    @GetMapping("/mentors/{mentorId}/events")
    public List<MentorEventResponse> all(@PathVariable Long mentorId) {
        List<MenteeToEvent> menteeToEvents = menteeToEventService.getByMentorId(mentorId);
        List<Resume> resumes = getResumes(menteeToEvents);
        List<UserResponse> mentees = getMentees(resumes);

        return MentorEventResponse.collect(menteeToEvents, resumes, mentees);
    }

    @GetMapping("/mentees/{menteeId}/events")
    public List<MenteeEventResponse> getOwnEvents(@PathVariable Long menteeId) {
        List<MenteeToEvent> menteeToEvents = menteeToEventService.getByMenteeId(menteeId);
        List<UserResponse> mentors = getMentors(menteeToEvents);
        List<Resume> resumes = getResumes(menteeToEvents);

        return new Response(menteeToEvents, mentors, resumes).responses();
    }

    private List<Resume> getResumes(List<MenteeToEvent> menteeToEvents) {
        List<Long> resumeIds = menteeToEvents.stream()
                .map(MenteeToEvent::getResumeId)
                .toList();

        return resumeService.getAll(resumeIds);
    }

    private List<UserResponse> getMentees(List<Resume> resumes) {
        List<Long> menteeIds = resumes.stream()
                .map(Resume::getMenteeId)
                .toList();

        return userProvider.getByIds(menteeIds);
    }

    private List<UserResponse> getMentors(List<MenteeToEvent> byMenteeId) {
        List<Long> mentorIds = byMenteeId.stream()
                .map(m -> m.getEvent().getMentorId())
                .toList();

        return userProvider.getByIds(mentorIds);
    }

}
