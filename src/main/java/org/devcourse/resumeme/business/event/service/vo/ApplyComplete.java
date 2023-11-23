package org.devcourse.resumeme.business.event.service.vo;

import org.devcourse.resumeme.business.event.domain.model.ApplimentComplete;
import org.devcourse.resumeme.business.event.domain.model.ApplimentUpdate;

public class ApplyComplete implements ApplyUpdateVo {

    private Long resumeId;

    private String completeMessage;

    public ApplyComplete(Long resumeId, String completeMessage) {
        this.resumeId = resumeId;
        this.completeMessage = completeMessage;
    }

    @Override
    public ApplimentUpdate toModel() {
        return new ApplimentComplete(resumeId, completeMessage);
    }

}
