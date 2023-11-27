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
    @Column(columnDefinition = "LONGTEXT")
    private String resumeData;

    @Lob
    @Getter
    @Column(columnDefinition = "LONGTEXT")
    private String commentData;

    private Long resumeId;

    public Snapshot(String resumeData, String commentData, Long resumeId) {
        this.resumeData = resumeData;
        this.commentData = commentData;
        this.resumeId = resumeId;
    }

    public void updateComment(String commentData) {
        this.commentData = commentData;
    }

    public String get(String type) {
        if (type.equals("resume")) {
            return resumeData;
        }

        return commentData;
    }

}
