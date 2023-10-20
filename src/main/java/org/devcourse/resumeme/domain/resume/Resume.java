package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.Position;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Resume {

    @Id
    @GeneratedValue
    @Column(name = "resume_id")
    private Long id;

    private String title;

    private Long menteeId;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Lob
    private String introduce;

    @Embedded
    private Training training;

    @Embedded
    private Career career;

    @Embedded
    private Project project;

    private String githubAddress;

    private String phoneNumber;

}
