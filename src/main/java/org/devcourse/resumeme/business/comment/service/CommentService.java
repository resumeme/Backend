package org.devcourse.resumeme.business.comment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.comment.domain.Comment;
import org.devcourse.resumeme.business.comment.repository.CommentRepository;
import org.devcourse.resumeme.business.resume.controller.career.dto.ComponentResponse;
import org.devcourse.resumeme.business.resume.entity.Snapshot;
import org.devcourse.resumeme.business.resume.repository.SnapshotRepository;
import org.devcourse.resumeme.global.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final SnapshotRepository snapshotRepository;

    public Comment create(Comment comment, Long resumeId) {
        List<Comment> comments = commentRepository.findAllByResumeId(resumeId);
        if (comments.isEmpty()) {
            try {
                // 이력서 원본 데이터 저장
                RestTemplate restTemplate = new RestTemplate();
                Map<String, List<ComponentResponse>> response = restTemplate.getForObject(new URI("http://localhost:8080/api/v1/resumes/" + resumeId), Map.class);
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(response);

                Optional<Snapshot> snapshot = snapshotRepository.findByResumeId(resumeId);
                if (snapshot.isPresent()) {
                    snapshot.get().updateResume(json);
                } else {
                    snapshotRepository.save(new Snapshot(json, resumeId));
                }
            } catch (URISyntaxException | JsonProcessingException e) {
                throw new CustomException("NOT_FOUND_DATA", "정보를 찾을 수 없습니다");
            }
        }
        return commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Comment getOne(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<Comment> getAllWithResumeId(Long resumeId) {
        return commentRepository.findAllByResumeId(resumeId);
    }

    public void update(String content, Long commentId) {
        Comment comment = getOne(commentId);
        comment.updateContent(content);
    }

    public void delete(Long commentId, Long resumeId) {
        Comment comment = getOne(commentId);
        comment.checkSameResume(resumeId);

        commentRepository.delete(comment);
    }

}
