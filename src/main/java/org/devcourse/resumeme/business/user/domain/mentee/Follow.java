package org.devcourse.resumeme.business.user.domain.mentee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@EqualsAndHashCode(exclude = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id
    @GeneratedValue
    @Column(name ="follow_id")
    private Long id;

    private Long menteeId;

    private Long mentorId;

    public Follow(Long menteeId, Long mentorId) {
        this.menteeId = menteeId;
        this.mentorId = mentorId;
    }

}
