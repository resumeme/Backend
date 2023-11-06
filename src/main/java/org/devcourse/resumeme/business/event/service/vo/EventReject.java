package org.devcourse.resumeme.business.event.service.vo;

public record EventReject(Long eventId, Long menteeId, String rejectMessage) {

}
