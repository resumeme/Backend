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
import org.devcourse.resumeme.common.util.Validator;
import org.devcourse.resumeme.global.advice.exception.ExceptionCode;

import static org.devcourse.resumeme.common.util.Validator.Condition.isBlank;
import static org.devcourse.resumeme.common.util.Validator.check;

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

    @Getter
    private String certificationTitle;

    @Getter
    private String acquisitionDate;

    @Getter
    private String issuingAuthority;

    @Getter
    private String link;

    @Getter
    private String description;

    public Certification(Resume resume, String certificationTitle, String acquisitionDate, String issuingAuthority, String link, String description) {
        Validator.check(isBlank(certificationTitle), ExceptionCode.NO_EMPTY_VALUE);

        this.resume = resume;
        this.certificationTitle = certificationTitle;
        this.acquisitionDate = acquisitionDate;
        this.issuingAuthority = issuingAuthority;
        this.link = link;
        this.description = description;
    }

}
