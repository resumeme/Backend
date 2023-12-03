package org.devcourse.resumeme.business.userevent.controller.dto;

import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.user.service.vo.UserResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record Response(List<MenteeEventResponse> responses) {

    public Response(List<MenteeToEvent> menteeToEvents, List<UserResponse> mentors, List<Resume> resumes) {
        this(getResponses(menteeToEvents, mentors, resumes));
    }

    private static List<MenteeEventResponse> getResponses(List<MenteeToEvent> menteeToEvents, List<UserResponse> mentors, List<Resume> resumes) {
        Map<Long, String> mentorsMap = mentors.stream()
                .collect(Collectors.toMap(UserResponse::userId, UserResponse::nickname));
        Map<Long, String> mentorEventMap = menteeToEvents.stream()
                .collect(Collectors.toMap(e -> e.getEvent().getId(), e -> mentorsMap.get(e.getEvent().getMentorId())));
        Map<Long, String> resumeTitles = resumes.stream()
                .collect(Collectors.toMap(Resume::getId, Resume::getTitle));

        return menteeToEvents.stream()
                .map(menteeToEvent -> {
                    Long eventId = menteeToEvent.getEvent().getId();
                    String nickname = mentorEventMap.get(eventId);
                    String resumeTitle = resumeTitles.get(menteeToEvent.getResumeId());

                    return new MenteeEventResponse(menteeToEvent, nickname, resumeTitle);
                })
                .toList();
    }

}
