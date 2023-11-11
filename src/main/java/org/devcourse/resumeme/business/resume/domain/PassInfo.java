package org.devcourse.resumeme.business.resume.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PassInfo {

    @Getter
    @Column(name = "pass_status")
    private boolean passStatus;

    @Getter
    @Column(name = "pass_date")
    private LocalDateTime passDate;

    public PassInfo(boolean passStatus, LocalDateTime passDate) {
        this.passStatus = passStatus;
        this.passDate = passDate;
    }

}
