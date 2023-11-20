package org.devcourse.resumeme.business.event.controller.dto.v2;

import org.devcourse.resumeme.business.event.service.vo.EventUpdateVo;

public abstract class EventUpdateRequest {

    public abstract EventUpdateVo toVo();

}
