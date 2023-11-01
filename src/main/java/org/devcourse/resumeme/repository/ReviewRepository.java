package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByResumeId(Long resumeId);

}
