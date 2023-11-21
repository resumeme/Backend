package org.devcourse.resumeme.business.userevent.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.event.service.EventPositionService;
import org.devcourse.resumeme.business.event.service.EventService;
import org.devcourse.resumeme.business.event.service.vo.AllEventFilter;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.business.userevent.controller.dto.MenteeEventResponse;
import org.devcourse.resumeme.business.userevent.controller.dto.MentorEventResponse;
import org.devcourse.resumeme.global.auth.model.jwt.JwtUser;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.devcourse.resumeme.global.exception.ExceptionCode.BAD_REQUEST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserEventController {

    private final EventService eventService;

    private final ResumeService resumeService;

    private final EventPositionService eventPositionService;

    @GetMapping("/mentors/{mentorId}/events")
    public List<MentorEventResponse> all(@PathVariable Long mentorId, @AuthenticationPrincipal JwtUser user) {
        if (!mentorId.equals(user.id())) {
            throw new CustomException(BAD_REQUEST);
        }

        return eventService.getAll(new AllEventFilter(mentorId, null)).stream()
                .map(event -> new MentorEventResponse(event, eventPositionService.getAll(event.getId()), getResumes(event)))
                .toList();
    }

    private List<Resume> getResumes(Event event) {
        List<MenteeToEvent> applicants = event.getApplicants();
        List<Long> menteeIds = applicants.stream()
                .map(MenteeToEvent::getMenteeId)
                .toList();
        List<Long> resumeIds = applicants.stream()
                .map(MenteeToEvent::getResumeId)
                .toList();

        return resumeService.getAll(resumeIds)
                .stream()
                .filter(resume -> menteeIds.contains(resume.menteeId()))
                .toList();
    }

    @GetMapping("/mentees/{menteeId}/events")
    public List<MenteeEventResponse> getOwnEvents(@PathVariable Long menteeId, @AuthenticationPrincipal JwtUser user) {
        if (!menteeId.equals(user.id())) {
            throw new CustomException(BAD_REQUEST);
        }

        List<Event> events = eventService.getAll(new AllEventFilter(null, menteeId));
        return events.stream()
                .flatMap(event -> event.getApplicants().stream()
                        .filter(applicant -> applicant.isSameMentee(user.id()))
                        .map(applicant -> new MenteeEventResponse(applicant, event))
                        .toList().stream())
                .toList();
    }

}
