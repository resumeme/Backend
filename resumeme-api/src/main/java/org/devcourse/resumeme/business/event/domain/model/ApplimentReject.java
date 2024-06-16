package org.devcourse.resumeme.business.event.domain.model;

import lombok.Getter;
import org.devcourse.resumeme.business.event.domain.Event;

public class ApplimentReject implements ApplimentUpdate {

    @Getter
    private Long menteeId;

    @Getter
    private String rejectMessage;

    public ApplimentReject(Long menteeId, String rejectMessage) {
        this.menteeId = menteeId;
        this.rejectMessage = rejectMessage;
    }


    @Override
    public Long update(Event event) {
        event.reject(menteeId, rejectMessage);

        return null;
    }

}
