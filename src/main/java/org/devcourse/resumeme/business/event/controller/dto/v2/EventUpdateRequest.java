package org.devcourse.resumeme.business.event.controller.dto.v2;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.devcourse.resumeme.business.event.service.vo.EventUpdateVo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = EventRejectRequestV2.class, name = "reject"),
        @JsonSubTypes.Type(value = CompleteEventRequestV2.class, name = "complete")
})
public abstract class EventUpdateRequest {

    protected String type;

    public abstract EventUpdateVo toVo();

}
