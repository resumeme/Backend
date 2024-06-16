package org.devcourse.resumeme.business.event.controller.dto.v2;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.event.service.vo.ApplyComplete;
import org.devcourse.resumeme.business.event.service.vo.ApplyReject;
import org.devcourse.resumeme.business.event.service.vo.ApplyUpdateVo;

@Data
@NoArgsConstructor
public class ApplyUpdateRequest {

    private Long menteeId;

    private String rejectMessage;

    private Long resumeId;

    private String completeMessage;

    public ApplyUpdateRequest(Long menteeId, String rejectMessage, Long resumeId, String completeMessage) {
        this.menteeId = menteeId;
        this.rejectMessage = rejectMessage;
        this.resumeId = resumeId;
        this.completeMessage = completeMessage;
    }

    public ApplyUpdateVo toVo() {
        if (rejectMessage != null) {
            return toReject();
        }

        return toComplete();
    }

    private ApplyUpdateVo toReject() {
        return new ApplyReject(menteeId, rejectMessage);
    }

    private ApplyUpdateVo toComplete() {
        return new ApplyComplete(resumeId, completeMessage);
    }

}
