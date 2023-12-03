package org.devcourse.resumeme.business.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.event.domain.EventStatus;
import org.devcourse.resumeme.business.event.repository.EventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventScheduler {

    private final EventRepository eventRepository;

    @Scheduled(cron = "0 0/1 * * * *")
    public void openBookedEvents() {
        log.info("scheduler 실행 됨");
        eventRepository.openBookedEvent(EventStatus.OPEN, LocalDateTime.now());
        eventRepository.closeApplyToEvent(EventStatus.CLOSE, LocalDateTime.now());
        eventRepository.finishEvent(EventStatus.FINISH, LocalDateTime.now());
    }

}
