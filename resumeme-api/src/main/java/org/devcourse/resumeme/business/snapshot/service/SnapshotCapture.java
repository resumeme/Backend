package org.devcourse.resumeme.business.snapshot.service;

public interface SnapshotCapture {

    void capture(Long eventId, Long resumeId);

}
