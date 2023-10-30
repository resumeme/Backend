package org.devcourse.resumeme.domain.admin;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class MentorApplicationEvent extends ApplicationEvent {

    @Getter
    private MentorApplication mentorApplication;

    public MentorApplicationEvent(Object source, MentorApplication mentorApplication) {
        super(source);
        this.mentorApplication = mentorApplication;
    }

}
