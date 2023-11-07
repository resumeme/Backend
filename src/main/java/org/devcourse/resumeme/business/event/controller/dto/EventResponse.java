package org.devcourse.resumeme.business.event.controller.dto;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventPosition;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;
import org.devcourse.resumeme.business.event.domain.MenteeToEvent;
import org.devcourse.resumeme.business.resume.domain.Resume;

import java.time.LocalDateTime;
import java.util.List;

public record EventResponse(EventInfoResponse info, List<ResumeResponse> resumes) {

    public EventResponse(Event event, List<EventPosition> positions, List<Resume> resumes) {
        this(new EventInfoResponse(event, positions), toResponse(event, resumes));
    }

    public static List<ResumeResponse> toResponse(Event event, List<Resume> resumes) {
        return event.getApplicants().stream()
                .flatMap(
                        applicant -> resumes.stream()
                                .filter(resume -> resume.getId().equals(applicant.getResumeId()))
                                .map(resume -> new ResumeResponse(applicant, resume))
                ).toList();
    }

    public record EventInfoResponse(String title, int maximumCount, int currentApplicantCount, List<String> positions, TimeInfo timeInfo) {

        public EventInfoResponse(Event event, List<EventPosition> positions) {
            this(event.title(), event.maximumCount(), event.getApplicants().size(),
                    convertToString(positions), new TimeInfo(event.getEventTimeInfo()));
        }

        private static List<String> convertToString(List<EventPosition> positions) {
            return positions.stream()
                    .map(eventPosition -> eventPosition.getPosition().name())
                    .toList();
        }

        record TimeInfo(LocalDateTime openDateTime, LocalDateTime closeDateTime, LocalDateTime endDate) {

            public TimeInfo(EventTimeInfo info) {
                this(info.getOpenDateTime(), info.getCloseDateTime(), info.getEndDate());
            }
        }

    }

    public record ResumeResponse(Long resumeId, String menteeName, String resumeTitle, String progressStatus) {

        public ResumeResponse(MenteeToEvent applicant, Resume resume) {
            this(resume.getId(), resume.menteeName(), resume.getTitle(), applicant.getProgress().name());
        }

    }

}
