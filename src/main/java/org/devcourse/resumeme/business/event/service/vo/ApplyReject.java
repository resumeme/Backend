package org.devcourse.resumeme.business.event.service.vo;

import org.devcourse.resumeme.business.event.domain.model.ApplimentReject;
import org.devcourse.resumeme.business.event.domain.model.ApplimentUpdate;

public final class ApplyReject implements ApplyUpdateVo {

    private final Long menteeId;

    private final String rejectMessage;

    public ApplyReject(Long menteeId, String rejectMessage) {
        this.menteeId = menteeId;
        this.rejectMessage = rejectMessage;
    }

    @Override
    public ApplimentUpdate toModel() {
        return new ApplimentReject(menteeId, rejectMessage);
    }

}
