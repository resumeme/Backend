package org.devcourse.resumeme.business.event.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.domain.EventPosition;
import org.devcourse.resumeme.business.event.repository.EventPositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class EventPositionService {

    private final EventPositionRepository eventPositionRepository;

    public List<EventPosition> getAll(Long eventId) {
        return eventPositionRepository.findAllByEventId(eventId);
    }

    public Map<Long, List<EventPosition>> getAll(List<Long> eventIds) {
        return eventPositionRepository.findAllByEventIds(eventIds).stream()
                .collect(groupingBy(position -> position.getEvent().getId(), toList()));
    }

}
