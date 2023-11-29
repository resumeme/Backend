package org.devcourse.resumeme.business.snapshot.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.snapshot.entity.Snapshot;
import org.devcourse.resumeme.business.snapshot.repository.SnapshotRepository;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.stereotype.Service;

import static org.devcourse.resumeme.global.exception.ExceptionCode.NOT_FOUND_SNAPSHOT;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    private final SnapshotRepository snapshotRepository;

    public Snapshot getByResumeId(Long resumeId) {
        return snapshotRepository.findByResumeId(resumeId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_SNAPSHOT));
    }

}
