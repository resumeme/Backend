package org.devcourse.resumeme.business.userevent.controller.dto;

import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record Response(List<MenteeEventResponse> responses) {

    public Response(List<MenteeToEvent> menteeToEvents, List<Mentor> mentors, List<Resume> resumes) {
        this(getResponses(menteeToEvents, mentors, resumes));
    }

    private static List<MenteeEventResponse> getResponses(List<MenteeToEvent> menteeToEvents, List<Mentor> mentors, List<Resume> resumes) {
        Map<Long, Mentor> mentorsMap = mentors.stream()
                .collect(Collectors.toMap(Mentor::getId, Function.identity()));
        Map<Long, Mentor> mentorEventMap = menteeToEvents.stream()
                .collect(Collectors.toMap(e -> e.getEvent().getId(), e -> mentorsMap.get(e.getEvent().getMentorId())));
        Map<Long, String> resumeTitles = resumes.stream()
                .collect(Collectors.toMap(Resume::getId, Resume::getTitle));

        return menteeToEvents.stream()
                .map(menteeToEvent -> {
                    Long eventId = menteeToEvent.getEvent().getId();
                    String nickname = mentorEventMap.get(eventId).getNickname();
                    String resumeTitle = resumeTitles.get(menteeToEvent.getResumeId());

                    return new MenteeEventResponse(menteeToEvent, nickname, resumeTitle);
                })
                .toList();
    }

}
