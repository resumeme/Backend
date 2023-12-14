package org.devcourse.resumeme.business.snapshot.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.comment.controller.dto.CommentWithReviewResponse;
import org.devcourse.resumeme.business.resume.controller.dto.AllComponentResponse;
import org.devcourse.resumeme.business.snapshot.entity.Snapshot;
import org.devcourse.resumeme.business.snapshot.repository.SnapshotRepository;
import org.devcourse.resumeme.common.util.ObjectMapperUtils;
import org.devcourse.resumeme.common.util.RestTemplateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.devcourse.resumeme.common.util.ObjectMapperUtils.getData;
import static org.devcourse.resumeme.common.util.RestTemplateUtils.getLocalResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCapture implements SnapshotCapture {

    private final SnapshotRepository snapshotRepository;

    @Override
    public void capture(Long eventId, Long resumeId) {
        if (resumeId == null) {
            return;
        }

        String uri = "/api/v1/events/%d/resumes/%d/comments".formatted(eventId, resumeId);
        CommentWithReviewResponse response = RestTemplateUtils.getLocalResponse(uri, CommentWithReviewResponse.class);
        String data = ObjectMapperUtils.getData(response);

        Optional<Snapshot> snapshotOption = snapshotRepository.findByResumeId(resumeId);
        if (snapshotOption.isPresent()) {
            Snapshot snapshot = snapshotOption.get();
            snapshot.updateComment(data);

            return;
        }

        AllComponentResponse componentResponse = getLocalResponse("/api/v1/resumes/" + resumeId, AllComponentResponse.class);
        String resumeData = getData(componentResponse);
        snapshotRepository.save(new Snapshot(resumeData, data, resumeId));
    }

}
