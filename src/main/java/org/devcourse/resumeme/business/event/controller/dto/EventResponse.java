package org.devcourse.resumeme.business.event.controller.dto;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.resume.domain.Resume;

import java.time.LocalDateTime;
import java.util.List;

public record EventResponse(EventInfoResponse info, List<ResumeResponse> resumes) {

    public EventResponse(Event event, List<Resume> resumes) {
        this(new EventInfoResponse(event), toResponse(event, resumes));
    }

    public static List<ResumeResponse> toResponse(Event event, List<Resume> resumes) {
        return event.getApplicants().stream()
                .flatMap(
                        applicant -> resumes.stream()
                                .filter(resume -> resume.getId().equals(applicant.getResumeId()))
                                .map(resume -> new ResumeResponse(applicant, resume))
                ).toList();
    }

    public record EventInfoResponse(String title, int maximumCount, LocalDateTime endDate) {

        public EventInfoResponse(Event event) {
            this(event.title(), event.maximumCount(), event.endDate());
        }

    }

    public record ResumeResponse(Long resumeId, String menteeName, String resumeTitle, String progressStatus) {

        public ResumeResponse(MenteeToEvent applicant, Resume resume) {
            this(resume.getId(), resume.menteeName(), resume.getTitle(), applicant.getProgress().name());
        }

    }

}
