package org.devcourse.resumeme.business.event.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.event.domain.EventPosition;
import org.devcourse.resumeme.business.event.repository.EventPositionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventPositionService {

    private final EventPositionRepository eventPositionRepository;

    public List<EventPosition> getAll(Long eventId) {
        return eventPositionRepository.findAllByEventId(eventId);
    }

    public List<EventPosition> getAll(List<Long> eventIds) {
        List<EventPosition> positions = new ArrayList<>(eventPositionRepository.findAllByEventIds(eventIds));
        Collections.sort(positions);

        return positions;
    }

}
