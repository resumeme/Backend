package org.devcourse.resumeme.controller;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.controller.dto.ReviewCreateRequest;
import org.devcourse.resumeme.controller.dto.ReviewResponse;
import org.devcourse.resumeme.domain.event.Event;
import org.devcourse.resumeme.domain.resume.Resume;
import org.devcourse.resumeme.domain.review.Review;
import org.devcourse.resumeme.service.EventService;
import org.devcourse.resumeme.service.ResumeService;
import org.devcourse.resumeme.service.ReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events/{eventId}/resume/{resumeId}/reviews")
public class ReviewController {

    private final ResumeService resumeService;

    private final ReviewService reviewService;

    private final EventService eventService;

    @PostMapping
    public ReviewResponse createReview(@PathVariable Long resumeId, @RequestBody ReviewCreateRequest request) {
        Resume resume = resumeService.getOne(resumeId);
        Review review = reviewService.create(request.toEntity(resume));

        return new ReviewResponse(review.getId(), review.getContent(), review.getType().name());
    }

    @GetMapping
    public List<ReviewResponse> getAllOwnReviews(@PathVariable Long eventId, @PathVariable Long resumeId) {
        Event event = eventService.getOne(eventId);
        event.checkAppliedResume(resumeId);

        return reviewService.getAllWithResumeId(resumeId).stream()
                .map(ReviewResponse::new)
                .toList();
    }

}
