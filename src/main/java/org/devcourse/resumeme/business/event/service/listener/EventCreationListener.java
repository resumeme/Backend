package org.devcourse.resumeme.business.event.service.listener;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.mail.service.EmailService;
import org.devcourse.resumeme.business.user.domain.mentee.Follow;
import org.devcourse.resumeme.business.user.entity.User;
import org.devcourse.resumeme.business.user.entity.UserService;
import org.devcourse.resumeme.business.user.service.mentee.FollowService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.devcourse.resumeme.business.mail.service.EmailInfoGenerator.createEventCreationMail;

@Component
@RequiredArgsConstructor
public class EventCreationListener implements ApplicationListener<EventCreation> {

    private final EmailService emailService;

    private final FollowService followService;

    private final UserService userService;

    @Override
    public void onApplicationEvent(EventCreation eventCreation) {
        Long mentorId = eventCreation.getEventNoticeInfo().mentorId();
        List<Long> followerIds = followService.getFollowers(mentorId)
                .stream()
                .map(Follow::getMenteeId)
                .toList();

        List<String> emails = userService.getByIds(followerIds).stream()
                .map(User::getEmail)
                .toList();

        emailService.sendEmail(createEventCreationMail(emails, eventCreation.getEventNoticeInfo()));
    }

}
