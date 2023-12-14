package org.devcourse.resumeme.business.userevent.controller.dto;

import org.devcourse.resumeme.business.event.controller.dto.EventInfoResponse;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.user.service.vo.UserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public record MentorEventResponse(EventInfoResponse info, List<ResumeResponse> resumes) {

    public MentorEventResponse(Event event, int size, List<Resume> resumes, List<UserResponse> mentees) {
        this(new EventInfoResponse(event, size, new ArrayList<>()), toResponse(event, resumes, mentees));
    }

    public static List<MentorEventResponse> collect(List<MenteeToEvent> menteeToEvents, List<Resume> resumes, List<UserResponse> mentees) {
        Map<Long, List<MenteeToEvent>> menteeToEventMap = menteeToEvents.stream()
                .collect(groupingBy(position -> position.getEvent().getId(), toList()));

        List<Event> events = menteeToEvents.stream()
                .map(MenteeToEvent::getEvent)
                .toList();

        return events.stream()
                .map(event -> new MentorEventResponse(event, menteeToEventMap.get(event.getId()).size(), resumes, mentees))
                .toList();
    }

    public static List<ResumeResponse> toResponse(Event event, List<Resume> resumes, List<UserResponse> mentees) {
        if (resumes.isEmpty()) {
            return List.of();
        }
        Map<Long, Resume> resumesMap = resumes.stream()
                .collect(Collectors.toMap(Resume::getId, Function.identity()));

        Map<Long, String> menteeMap = mentees.stream()
                .collect(Collectors.toMap(UserResponse::userId, UserResponse::name));

        Map<Long, String> menteeNames = resumes.stream()
                .map(resume -> new MenteeEvent(resume.getId(), menteeMap.get(resume.getMenteeId())))
                .collect(Collectors.toMap(MenteeEvent::resumeId, MenteeEvent::menteeName));


        return event.getApplicants().stream()
                .map(applicant -> {
                    Resume resume = resumesMap.get(applicant.getResumeId());
                    String menteeName = menteeNames.get(applicant.getResumeId());

                    return new ResumeResponse(applicant, resume, menteeName);
                })
                .toList();
    }

    public record ResumeResponse(Long resumeId, String menteeName, String resumeTitle, String progressStatus) {

        public ResumeResponse(MenteeToEvent applicant, Resume resume, String menteeName) {
            this(resume.getId(), menteeName, resume.getTitle(), applicant.getProgress().name());
        }

    }

    public record MenteeEvent(Long resumeId, String menteeName) {

    }

}
