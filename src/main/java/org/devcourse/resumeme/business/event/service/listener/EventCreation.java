package org.devcourse.resumeme.business.event.service.listener;

import lombok.Getter;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.user.service.vo.UserResponse;
import org.springframework.context.ApplicationEvent;

public class EventCreation extends ApplicationEvent {

    @Getter
    private final EventNoticeInfo eventNoticeInfo;

    public EventCreation(Object source, EventNoticeInfo eventNoticeInfo) {
        super(source);
        this.eventNoticeInfo = eventNoticeInfo;
    }

    public record EventNoticeInfo(Long eventId, Long mentorId, String mentorNickname) {

        public EventNoticeInfo(Event event, UserResponse mentor) {
            this(event.getId(), mentor.userId(), mentor.nickname());
        }

    }

}
