package org.devcourse.resumeme.business.userevent.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.event.service.EventService;
import org.devcourse.resumeme.business.event.service.MenteeToEventService;
import org.devcourse.resumeme.business.event.service.vo.AllEventFilter;
import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.business.user.entity.UserService;
import org.devcourse.resumeme.business.userevent.controller.dto.MenteeEventResponse;
import org.devcourse.resumeme.business.userevent.controller.dto.MentorEventResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserEventController {

    private final EventService eventService;

    private final ResumeService resumeService;

    private final UserService userService;

    private final MenteeToEventService menteeToEventService;

    @GetMapping("/mentors/{mentorId}/events")
    public List<MentorEventResponse> all(@PathVariable Long mentorId) {
        Page<Event> events = eventService.getAllWithPage(new AllEventFilter(mentorId, null), Pageable.unpaged());
        List<Resume> resumes = getResumes(events.getContent());
        List<Mentee> mentees = getMentees(resumes);

        return events.stream()
                .map(event -> new MentorEventResponse(event, resumes, mentees))
                .toList();
    }

    private List<Resume> getResumes(List<Event> events) {
        List<Long> resumeIds = events.stream()
                .flatMap(event -> event.getApplicants().stream())
                .map(MenteeToEvent::getResumeId)
                .toList();

        return resumeService.getAll(resumeIds);
    }

    private List<Mentee> getMentees(List<Resume> resumes) {
        List<Long> menteeIds = resumes.stream()
                .map(Resume::getMenteeId)
                .toList();

        return userService.getByIds(menteeIds).stream()
                .map(User::toMentee)
                .toList();
    }

    @GetMapping("/mentees/{menteeId}/events")
    public List<MenteeEventResponse> getOwnEvents(@PathVariable Long menteeId) {
        List<MenteeToEvent> byMenteeId = menteeToEventService.getByMenteeId(menteeId);
        List<Event> events = byMenteeId.stream()
                .map(MenteeToEvent::getEvent)
                .toList();
        List<Long> mentorIds = byMenteeId.stream()
                .map(m -> m.getEvent().getMentorId())
                .toList();

        Map<Long, Mentor> mentors = userService.getByIds(mentorIds).stream()
                .map(User::toMentor)
                .collect(Collectors.toMap(Mentor::getId, Function.identity()));

        Map<Long, Mentor> mentorEventMap = byMenteeId.stream()
                .collect(Collectors.toMap(e -> e.getEvent().getId(), e -> mentors.get(e.getEvent().getMentorId())));
        Map<Long, String> resumeTitles = getResumes(events).stream()
                .collect(Collectors.toMap(Resume::getId, Resume::getTitle));

        return byMenteeId.stream()
                .map(menteeToEvent -> {
                    Long mentorId = menteeToEvent.getEvent().getId();
                    String nickname = mentorEventMap.get(mentorId).getNickname();
                    String resumeTitle = resumeTitles.get(menteeToEvent.getResumeId());

                    return new MenteeEventResponse(menteeToEvent, nickname, resumeTitle);
                })
                .toList();
    }

}
