package org.devcourse.resumeme.business.userevent.controller.dto;

import org.devcourse.resumeme.business.event.controller.dto.EventInfoResponse;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventPosition;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.resume.entity.Resume;

import java.util.List;

public record MentorEventResponse(EventInfoResponse info,  List<ResumeResponse> resumes) {

    public MentorEventResponse(Event event, List<EventPosition> positions, List<Resume> resumes) {
        this(new EventInfoResponse(event, positions), toResponse(event, resumes));
    }

    public static List<ResumeResponse> toResponse(Event event, List<Resume> resumes) {
        if (resumes == null) {
            return List.of();
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

}
