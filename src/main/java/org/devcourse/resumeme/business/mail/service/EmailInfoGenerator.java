package org.devcourse.resumeme.business.mail.service;

import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.mail.EmailInfo;
import org.devcourse.resumeme.business.user.domain.mentee.Mentee;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static org.devcourse.resumeme.business.mail.EmailType.EVENT_OPEN;
import static org.devcourse.resumeme.business.mail.EmailType.MENTOR_APPROVED;

@Component
public class EmailInfoGenerator {

    private static final String BASE_URL = "https://resumeme.vercel.app";

    public static EmailInfo createMentorApprovalMail(Mentor mentor) {
        String[] to = {mentor.getEmail()};
        Map<String, Object> attributes = Map.of("link", BASE_URL);

        return new EmailInfo(to, MENTOR_APPROVED, attributes);
    }

    public static EmailInfo createEventOpenMail(List<Mentee> mentees, Event event) {
        List<String> menteeList  = mentees.stream().map(Mentee::getEmail).toList();
        String[] to = menteeList.toArray(new String[menteeList.size()]);
        Map<String, Object> attributes = Map.of("link", BASE_URL + "/event/" + event.getId());

        return new EmailInfo(to, EVENT_OPEN, attributes);
    }

}
