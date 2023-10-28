package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
