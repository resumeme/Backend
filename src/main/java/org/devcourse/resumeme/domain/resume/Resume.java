package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import org.devcourse.resumeme.common.domain.Position;

@Entity
@Table(name = "RESUME")
public class Resume {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Lob
    private String introduce;

    @Embedded
    private Training training;

    @Embedded
    private Carrer carrer;

    @Embedded
    private Project project;

    private String githubAddress;

    private String phoneNumber;

}
