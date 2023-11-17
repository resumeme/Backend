package org.devcourse.resumeme.business.resume.repository;

public interface ComponentCustomRepository {

    void copy(Long originResumeId, Long newResumeId);

}
