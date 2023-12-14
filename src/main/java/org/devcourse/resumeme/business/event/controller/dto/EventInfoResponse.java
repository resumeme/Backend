package org.devcourse.resumeme.business.event.controller.dto;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.domain.EventPosition;
import org.devcourse.resumeme.business.event.domain.EventTimeInfo;

import java.time.LocalDateTime;
import java.util.List;

public record EventInfoResponse(Long id, Long mentorId, String title, String content, int maximumCount, int currentApplicantCount, String status, List<String> positions, TimeInfo timeInfo) {

    public EventInfoResponse(Event event, List<EventPosition> positions) {
        this(event.getId(), event.getMentorId(), event.title(), event.content(), event.maximumCount(), event.getApplicants().size(),
                event.getEventInfo().getStatus().name(), convertToString(positions), new TimeInfo(event.getEventTimeInfo()));
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
