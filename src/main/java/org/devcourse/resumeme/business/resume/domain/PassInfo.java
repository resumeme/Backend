package org.devcourse.resumeme.business.resume.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PassInfo {

    @Id
    @GeneratedValue
    @Column(name = "pass_info_id")
    private Long id;

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
