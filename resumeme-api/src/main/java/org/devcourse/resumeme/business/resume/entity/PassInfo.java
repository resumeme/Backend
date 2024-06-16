package org.devcourse.resumeme.business.resume.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Resume;

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

    @OneToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    public PassInfo(boolean passStatus, LocalDateTime passDate, Resume resume) {
        this.passStatus = passStatus;
        this.passDate = passDate;
        this.resume = resume;
    }

}
