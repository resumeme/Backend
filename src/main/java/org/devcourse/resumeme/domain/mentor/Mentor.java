package org.devcourse.resumeme.domain.mentor;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.devcourse.resumeme.domain.user.BasicInfo;

@Entity
public class Mentor {

    @Id
    @GeneratedValue
    @Column(name = "mentor_id")
    private Long id;

    @Embedded
    private BasicInfo basicInfo;

    private String desiredPosition; // 분야

    private String career; // 경력

    private String introduce; // 자기소개

}
