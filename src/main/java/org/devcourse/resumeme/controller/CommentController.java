package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.controller.dto.CommentCreateRequest;
import org.devcourse.resumeme.controller.dto.CommentResponse;
import org.devcourse.resumeme.domain.event.Event;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.domain.comment.Comment;
import org.devcourse.resumeme.service.EventService;
import org.devcourse.resumeme.service.ResumeService;
import org.devcourse.resumeme.service.CommentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events/{eventId}/resume/{resumeId}/comments")
public class CommentController {

    private final ResumeService resumeService;

    private final CommentService commentService;

    private final EventService eventService;

    @PostMapping
    public CommentResponse createReview(@PathVariable Long resumeId, @RequestBody CommentCreateRequest request) {
        Resume resume = resumeService.getOne(resumeId);
        Comment review = commentService.create(request.toEntity(resume));

        return new CommentResponse(review.getId(), review.getContent(), review.getType().name());
    }

    @GetMapping
    public List<CommentResponse> getAllOwnReviews(@PathVariable Long eventId, @PathVariable Long resumeId) {
        Event event = eventService.getOne(eventId);
        event.checkAppliedResume(resumeId);

        return commentService.getAllWithResumeId(resumeId).stream()
                .map(CommentResponse::new)
                .toList();
    }

}
