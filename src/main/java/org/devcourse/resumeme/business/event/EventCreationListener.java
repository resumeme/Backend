package org.devcourse.resumeme.business.event;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.mail.service.EmailService;
import org.devcourse.resumeme.business.user.domain.mentee.Follow;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.service.mentee.FollowService;
import org.devcourse.resumeme.business.user.service.mentee.MenteeService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.devcourse.resumeme.business.mail.service.EmailInfoGenerator.createEventCreationMail;

@Component
@RequiredArgsConstructor
public class EventCreationListener implements ApplicationListener<EventCreation> {

    private final EmailService emailService;

    private final MenteeService menteeService;

    private final FollowService followService;

    @Override
    public void onApplicationEvent(EventCreation eventCreation) {
        Long mentorId = eventCreation.getEventNoticeInfo().mentorId();
        List<Long> followerIds = followService.getFollowers(mentorId)
                .stream()
                .map(Follow::getMenteeId)
                .toList();
        List<Mentee> followers = menteeService.getAllIn(followerIds);

        emailService.sendEmail(createEventCreationMail(followers, eventCreation.getEventNoticeInfo()));
    }

}
