package org.devcourse.resumeme.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.domain.review.Review;
import org.devcourse.resumeme.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review create(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getAllWithResumeId(Long resumeId) {
        return reviewRepository.findAllByResumeId(resumeId);
    }

}
