package org.devcourse.resumeme.common.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Getter
    @CreatedDate
    protected LocalDateTime createdDate;

    @LastModifiedDate
    protected LocalDateTime lastModifiedDate;

    protected BaseEntity() {
    }

    protected BaseEntity(LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public LocalDateTime getLastModifiedDate() {
        if (lastModifiedDate == null) {
            return createdDate;
        }

        return lastModifiedDate;
    }

}
