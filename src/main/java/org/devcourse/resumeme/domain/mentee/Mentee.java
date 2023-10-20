package org.devcourse.resumeme.domain.mentee;


import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.devcourse.resumeme.domain.user.BasicInfo;

@Entity
public class Mentee {

    @Id
    @GeneratedValue
    @Column(name = "mentee_id")
    private Long id;

    @Embedded
    private BasicInfo basicInfo;

    private String position; // 희망 분야

    private String industry; // 희망 도메인

    private String introduce; // 경험 및 기술

}
