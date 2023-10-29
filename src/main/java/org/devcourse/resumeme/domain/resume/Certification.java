package org.devcourse.resumeme.domain.resume;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

import static org.devcourse.resumeme.common.util.Validator.Condition.isBlank;
import static org.devcourse.resumeme.common.util.Validator.validate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Certification {

    @Id
    @Getter
    @GeneratedValue
    @Column(name = "certification_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    private String certificationTitle;

    private String acquisitionDate;

    private String issuingAuthority;

    private String link;

    private String description;

    public Certification(String certificationTitle, String acquisitionDate, String issuingAuthority, String link, String description) {
        validate(isBlank(certificationTitle), ExceptionCode.NO_EMPTY_VALUE);

        this.certificationTitle = certificationTitle;
        this.acquisitionDate = acquisitionDate;
        this.issuingAuthority = issuingAuthority;
        this.link = link;
        this.description = description;
    }

}
