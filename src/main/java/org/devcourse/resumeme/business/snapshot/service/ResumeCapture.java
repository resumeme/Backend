package org.devcourse.resumeme.business.snapshot.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.controller.dto.AllComponentResponse;
import org.devcourse.resumeme.business.snapshot.entity.Snapshot;
import org.devcourse.resumeme.business.snapshot.repository.SnapshotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.devcourse.resumeme.common.util.ObjectMapperUtils.getData;
import static org.devcourse.resumeme.common.util.RestTemplateUtils.getLocalResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class ResumeCapture implements SnapshotCapture {

    private final SnapshotRepository snapshotRepository;

    @Override
    public void capture(Long eventId, Long resumeId) {
        AllComponentResponse response = getLocalResponse("/api/v1/resumes/" + resumeId, AllComponentResponse.class);
        String data = getData(response);

        Optional<Snapshot> snapshot = snapshotRepository.findByResumeId(resumeId);

        if (snapshot.isEmpty()) {
            snapshotRepository.save(new Snapshot(data, null, resumeId));
        }
    }

}
