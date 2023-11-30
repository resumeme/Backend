package org.devcourse.resumeme.business.user.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.business.user.domain.admin.MentorApplication;
import org.devcourse.resumeme.business.user.domain.admin.MentorApplicationEvent;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import static org.devcourse.resumeme.global.exception.ExceptionCode.MENTOR_ALREADY_APPROVED;

@Slf4j
@Component
@RequiredArgsConstructor
public class MentorApplicationEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishMentorApplicationEvent(Mentor mentor) {
        if (mentor.isApproved()) {
            throw new CustomException(MENTOR_ALREADY_APPROVED);
        }
        MentorApplicationEvent mentorApplicationEvent = new MentorApplicationEvent(this, new MentorApplication(mentor.getId()));
        applicationEventPublisher.publishEvent(mentorApplicationEvent);
    }

}
