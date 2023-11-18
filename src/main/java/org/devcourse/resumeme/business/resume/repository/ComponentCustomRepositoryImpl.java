package org.devcourse.resumeme.business.resume.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ComponentCustomRepositoryImpl implements ComponentCustomRepository{

    private final EntityManager em;

    public void copy(Long originResumeId, Long newResumeId) {
        em.createQuery("""
                insert into Component (
                    property, content, startDate, endDate, resumeId, component, createdDate, lastModifiedDate
                )
                select c.property,c.content,c.startDate,c.endDate, :newResumeId, c.component,c.createdDate,c.lastModifiedDate
                from Component c
                where c.resumeId = :originResumeId
                """)
                .setParameter("originResumeId", originResumeId)
                .setParameter("newResumeId", newResumeId);
    }

}
