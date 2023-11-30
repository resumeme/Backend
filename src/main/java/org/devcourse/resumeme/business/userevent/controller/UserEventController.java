package org.devcourse.resumeme.business.userevent.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.event.service.EventPositionService;
import org.devcourse.resumeme.business.event.service.EventService;
import org.devcourse.resumeme.business.event.service.MenteeToEventService;
import org.devcourse.resumeme.business.event.service.vo.AllEventFilter;
import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.business.userevent.controller.dto.MenteeEventResponse;
import org.devcourse.resumeme.business.userevent.controller.dto.MentorEventResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserEventController {

    private final EventService eventService;

    private final ResumeService resumeService;

    private final EventPositionService eventPositionService;

    private final MenteeToEventService menteeToEventService;

    @GetMapping("/mentors/{mentorId}/events")
    public List<MentorEventResponse> all(@PathVariable Long mentorId) {
        return eventService.getAllWithPage(new AllEventFilter(mentorId, null), Pageable.unpaged()).stream()
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
    public List<MenteeEventResponse> getOwnEvents(@PathVariable Long menteeId) {
        return menteeToEventService.getByMenteeId(menteeId).stream()
                .map(MenteeEventResponse::new)
                .toList();
    }

}
