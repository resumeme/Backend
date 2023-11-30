package org.devcourse.resumeme.business.snapshot.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.comment.controller.dto.CommentWithReviewResponse;
import org.devcourse.resumeme.business.resume.controller.dto.AllComponentResponse;
import org.devcourse.resumeme.business.snapshot.service.vo.SnapshotVo;
import org.devcourse.resumeme.business.snapshot.entity.Snapshot;
import org.devcourse.resumeme.business.snapshot.entity.SnapshotType;
import org.devcourse.resumeme.business.snapshot.repository.SnapshotRepository;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.stereotype.Service;

import static org.devcourse.resumeme.common.util.ObjectMapperUtils.getObjectMapper;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NOT_FOUND_SNAPSHOT;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    private final SnapshotRepository snapshotRepository;

    public SnapshotVo getByResumeId(Long resumeId, SnapshotType type) {
        Snapshot snapshot = snapshotRepository.findByResumeId(resumeId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_SNAPSHOT));
        String data = snapshot.get(type);

        return switch (type) {
            case RESUME -> {
                AllComponentResponse response = getObjectMapper(data, AllComponentResponse.class);

                yield SnapshotVo.of(response);
            }
            case COMMENT -> {
                CommentWithReviewResponse response = getObjectMapper(data, CommentWithReviewResponse.class);

                yield SnapshotVo.of(response);
            }
        };
    }

}
