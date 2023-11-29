package org.devcourse.resumeme.business.comment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.comment.controller.dto.CommentWithReviewResponse;
import org.devcourse.resumeme.business.snapshot.entity.Snapshot;
import org.devcourse.resumeme.business.snapshot.repository.SnapshotRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentSnapEventListener implements ApplicationListener<CommentSnapEvent> {

    private final SnapshotRepository snapshotRepository;

    @Override
    public void onApplicationEvent(CommentSnapEvent event) {
        Long resumeId = event.getResumeId();
        Long eventId = event.getEventId();

        try {
            RestTemplate restTemplate = new RestTemplate();
            CommentWithReviewResponse response = restTemplate.getForObject(new URI("http://localhost:8080/api/v1/events/" + eventId + "/resumes/" + resumeId + "/comments"), CommentWithReviewResponse.class);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            String commentData = objectMapper.writeValueAsString(response);
            Optional<Snapshot> snapshotOption = snapshotRepository.findByResumeId(resumeId);
            if (snapshotOption.isPresent()) {
                Snapshot snapshot = snapshotOption.get();
                snapshot.updateComment(commentData);

                return;
            }

            snapshotRepository.save(new Snapshot(null, commentData, resumeId));
        } catch (JsonProcessingException | URISyntaxException e) {
            throw new RuntimeException(e);
        } 
    }

}
