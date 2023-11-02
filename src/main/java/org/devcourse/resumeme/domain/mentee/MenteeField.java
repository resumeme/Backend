package org.devcourse.resumeme.domain.mentee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.Field;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.check;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class MenteeField {

    @Id
    @GeneratedValue
    @Column(name = "mentee_field_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "mentee_id")
    private Mentee mentee;

    @Getter
    @Enumerated(STRING)
    private Field field;

    public MenteeField(Mentee mentee, Field field) {
        check(field == null, "NO_EMPTY_VALUE", "분야는 필수 값입니다");
        check(mentee == null, "NO_EMPTY_VALUE", "사용자는 필수 값입니다");

        this.mentee = mentee;
        this.field = field;
    }

}
