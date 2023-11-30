package org.devcourse.resumeme.business.comment.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.comment.controller.dto.CommentCreateRequest;
import org.devcourse.resumeme.business.comment.controller.dto.CommentResponse;
import org.devcourse.resumeme.business.comment.controller.dto.CommentUpdateRequest;
import org.devcourse.resumeme.business.comment.controller.dto.CommentWithReviewResponse;
import org.devcourse.resumeme.business.comment.domain.Comment;
import org.devcourse.resumeme.business.comment.service.CommentService;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.event.service.EventService;
import org.devcourse.resumeme.business.resume.entity.Resume;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events/{eventId}/resumes/{resumeId}/comments")
public class CommentController {

    private final ResumeService resumeService;

    private final CommentService commentService;

    private final EventService eventService;

    @PostMapping
    public CommentResponse createReview(@PathVariable Long eventId, @PathVariable Long resumeId, @RequestBody CommentCreateRequest request) {
        eventService.checkCommentAvailableDate(eventId);
        Resume resume = resumeService.getOne(resumeId);
        Comment review = commentService.create(request.toEntity(resume), resumeId);

        return new CommentResponse(review.getId(), review.getContent(), request.componentId(), review.getLastModifiedDate());
    }

    @GetMapping
    public CommentWithReviewResponse getAllOwnReviews(@PathVariable Long eventId, @PathVariable Long resumeId) {
        Event event = eventService.getOne(eventId);
        event.checkAppliedResume(resumeId);

        List<CommentResponse> commentResponses = commentService.getAllWithResumeId(resumeId).stream()
                .map(CommentResponse::new)
                .toList();
        String overallReview = eventService.getOverallReview(event, resumeId);

        return new CommentWithReviewResponse(commentResponses, overallReview, event.getMentorId());
    }

    @PatchMapping("/{commentId}")
    public void updateComment(@RequestBody CommentUpdateRequest request, @PathVariable Long commentId) {
        commentService.update(request.content(), commentId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId, @PathVariable Long resumeId) {
        commentService.delete(commentId, resumeId);
    }

}
