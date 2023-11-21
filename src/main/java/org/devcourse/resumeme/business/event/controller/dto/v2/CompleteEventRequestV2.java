package org.devcourse.resumeme.business.event.controller.dto.v2;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.devcourse.resumeme.business.event.service.vo.EventCompleteV2;
import org.devcourse.resumeme.business.event.service.vo.EventUpdateVo;

@JsonTypeName("complete")
public class CompleteEventRequestV2 extends EventUpdateRequest {

    private Long resumeId;

    private String completeMessage;

    public CompleteEventRequestV2(Long resumeId, String completeMessage) {
        this.resumeId = resumeId;
        this.completeMessage = completeMessage;
    }

    @Override
    public EventUpdateVo toVo() {
        return new EventCompleteV2(resumeId, completeMessage);
    }

}
