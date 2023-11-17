package org.devcourse.resumeme.business.event.controller.dto;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventPosition;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;

import java.time.LocalDateTime;
import java.util.List;

public record EventResponse(EventInfoResponse info, MentorInfo mentorInfo, List<ResumeResponse> resumes) {

    public EventResponse(Event event, List<EventPosition> positions, List<Resume> resumes) {
        this(new EventInfoResponse(event, positions), new MentorInfo(event.getMentor()), toResponse(event, resumes));
    }

    public static List<ResumeResponse> toResponse(Event event, List<Resume> resumes) {
        if (resumes == null) {
            return null;
        }

        return event.getApplicants().stream()
                .flatMap(
                        applicant -> resumes.stream()
                                .filter(resume -> resume.getId().equals(applicant.getResumeId()))
                                .map(resume -> new ResumeResponse(applicant, resume))
                ).toList();
    }

    public record ResumeResponse(Long resumeId, String menteeName, String resumeTitle, String progressStatus) {

        public ResumeResponse(MenteeToEvent applicant, Resume resume) {
            this(resume.getId(), resume.menteeName(), resume.getTitle(), applicant.getProgress().name());
        }

    }

    record MentorInfo(Long mentorId, String nickname, String imageUrl) {

        MentorInfo(Mentor mentor) {
            this(mentor.getId(), mentor.getRequiredInfo().getNickname(), mentor.getImageUrl());
        }
    }

}
