package org.devcourse.resumeme.business.userevent.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.event.service.EventPositionService;
import org.devcourse.resumeme.business.event.service.EventService;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.business.userevent.controller.dto.MenteeResumeResponse;
import org.devcourse.resumeme.business.userevent.controller.dto.MentorEventResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserEventController {

    private final EventService eventService;

    private final ResumeService resumeService;

    private final EventPositionService eventPositionService;

    @GetMapping("/mentors/{mentorId}/events")
    public List<MentorEventResponse> all(@PathVariable Long mentorId) {
        return eventService.getAll(mentorId).stream()
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
    public List<MenteeResumeResponse> menteeResume(@PathVariable Long menteeId) {
        List<Resume> resumes = resumeService.getAllByMenteeId(menteeId);
        List<Resume> copiedResume = resumes.stream()
                .filter(Resume::isCopied)
                .toList();

        Map<Long, List<Long>> collect = new HashMap<>();
        for (Resume resume : copiedResume) {
            List<Long> copiedResumeId = collect.getOrDefault(resume.getOriginResumeId(), new ArrayList<>());
            copiedResumeId.add(resume.getId());

            collect.put(resume.getOriginResumeId(), copiedResumeId);
        }

        List<Long> resumeIds = copiedResume.stream()
                .map(Resume::getId)
                .toList();

        List<Event> applicantedEvent = eventService.getAll(resumeIds);
        Map<List<Long>, MenteeResumeResponse> result = new HashMap<>();

        List<Resume> originResumes = resumes.stream()
                .filter(Resume::isOrigin)
                .toList();

        for (Resume resume : originResumes) {
            result.put(collect.get(resume.getId()), new MenteeResumeResponse(resume.getId(), resume.getTitle(), resume.getLastModifiedDate(), new ArrayList<>()));
        }

        for (Event event : applicantedEvent) {
            Long copiedResumeId = event.getApplicants().get(0).getResumeId();

            result.keySet().stream()
                    .filter(ids -> ids.contains(copiedResumeId))
                    .map(result::get)
                    .forEach(menteeResumeResponse -> menteeResumeResponse.add(event));
        }

        return result.values().stream().toList();
    }

}
