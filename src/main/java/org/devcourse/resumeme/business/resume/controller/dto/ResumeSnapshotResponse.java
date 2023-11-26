package org.devcourse.resumeme.business.resume.controller.dto;

import lombok.Data;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;

import java.util.List;
import java.util.Map;

@Data
public class ResumeSnapshotResponse implements SnapshotResponse {

    private Map<String, List<ComponentResponse>> resumeData;

    public ResumeSnapshotResponse(Map<String, List<ComponentResponse>> resumeData) {
        this.resumeData = resumeData;
    }

}
