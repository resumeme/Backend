package org.devcourse.resumeme.business.comment.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.comment.controller.dto.CommentCreateRequest;
import org.devcourse.resumeme.business.comment.controller.dto.CommentResponse;
import org.devcourse.resumeme.business.comment.controller.dto.CommentUpdateRequest;
import org.devcourse.resumeme.business.event.domain.Event;
import org.devcourse.resumeme.business.resume.domain.Resume;
import org.devcourse.resumeme.business.comment.domain.Comment;
import org.devcourse.resumeme.business.event.service.EventService;
import org.devcourse.resumeme.business.resume.entity.Component;
import org.devcourse.resumeme.business.resume.service.ComponentService;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.devcourse.resumeme.business.comment.service.CommentService;
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

    private final ComponentService componentService;

    @PostMapping
    public CommentResponse createReview(@PathVariable Long resumeId, @RequestBody CommentCreateRequest request) {
        Resume resume = resumeService.getOne(resumeId);
        Component component = componentService.getOne(request.componentId());
        Comment review = commentService.create(request.toEntity(resume, component));

        return new CommentResponse(review.getId(), review.getContent(), component.getId(), review.getLastModifiedDate());
    }

    @GetMapping
    public List<CommentResponse> getAllOwnReviews(@PathVariable Long eventId, @PathVariable Long resumeId) {
        Event event = eventService.getOne(eventId);
        event.checkAppliedResume(resumeId);

        return commentService.getAllWithResumeId(resumeId).stream()
                .map(CommentResponse::new)
                .toList();
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
