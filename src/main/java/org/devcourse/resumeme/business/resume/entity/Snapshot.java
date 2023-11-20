package org.devcourse.resumeme.business.resume.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Snapshot {

    @Id
    @GeneratedValue
    @Column(name = "snapshot_id")
    private Long id;

    @Lob
    @Getter
    private String resumeData;

    private Long resumeId;

    public Snapshot(String resumeData, Long resumeId) {
        this.resumeData = resumeData;
        this.resumeId = resumeId;
    }

    public void updateResume(String json) {
        this.resumeData = json;
    }

}
