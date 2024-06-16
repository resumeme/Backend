package org.devcourse.resumeme.business.user.service.admin;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.user.domain.admin.MentorApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MentorApplicationEventListener implements ApplicationListener<MentorApplicationEvent> {

    private final MentorApplicationService service;

    @Override
    public void onApplicationEvent(MentorApplicationEvent event) {
        service.create(event.getMentorApplication());
    }

}
