package org.devcourse.resumeme.business.mail.service;

import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.mail.EmailInfo;
import org.devcourse.resumeme.business.user.domain.mentor.Mentor;

import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;
import static org.devcourse.resumeme.business.event.service.listener.EventCreation.EventNoticeInfo;
import static org.devcourse.resumeme.business.mail.EmailType.EVENT_CREATED;
import static org.devcourse.resumeme.business.mail.EmailType.MENTOR_APPROVED;

@NoArgsConstructor(access = PRIVATE)
public class EmailInfoGenerator {

    private static final String BASE_URL = "https://resumeme.vercel.app";

    public static EmailInfo createMentorApprovalMail(Mentor mentor) {
        String[] to = {mentor.getEmail()};
        Map<String, Object> attributes = Map.of("link", BASE_URL, "mentorRealName", mentor.getRequiredInfo().getRealName());

        return new EmailInfo(to, MENTOR_APPROVED, attributes);
    }

    public static EmailInfo createEventCreationMail(List<String> emails, EventNoticeInfo eventCreationInfo) {
        String[] to = emails.toArray(new String[emails.size()]);
        Map<String, Object> attributes = Map.of("link", BASE_URL + "/event/" + eventCreationInfo.eventId(), "mentorNickname", eventCreationInfo.mentorNickname());

        return new EmailInfo(to, EVENT_CREATED, attributes);
    }

}
