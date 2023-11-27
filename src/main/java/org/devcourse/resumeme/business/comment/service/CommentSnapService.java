package org.devcourse.resumeme.business.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentSnapService {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void snapComment(Long resumeId, Long eventId) {
        applicationEventPublisher.publishEvent(new CommentSnapEvent(this, resumeId, eventId));
    }

}
