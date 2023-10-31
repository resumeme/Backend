package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.domain.admin.MentorApplication;
import org.devcourse.resumeme.domain.admin.MentorApplicationEvent;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MentorApplicationEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishMentorApplicationEvent(Mentor mentor) {
        if (mentor.isApproved()) {
            throw new CustomException(ExceptionCode.MENTOR_ALREADY_APPROVED);
        }
        MentorApplicationEvent mentorApplicationEvent = new MentorApplicationEvent(this, new MentorApplication(mentor));
        applicationEventPublisher.publishEvent(mentorApplicationEvent);
    }

}
