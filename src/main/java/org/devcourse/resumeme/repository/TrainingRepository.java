package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.resume.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {

}
