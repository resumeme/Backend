package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.devcourse.resumeme.common.domain.Position;

import java.time.LocalDate;

@Embeddable
public class Carrer {

    private String companyName;

    @Enumerated(EnumType.STRING)
    private Position position;

    private LocalDate startDate;

    @Column(nullable = true)
    private LocalDate endDate;

    private String content;

}
