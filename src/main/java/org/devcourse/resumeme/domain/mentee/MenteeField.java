package org.devcourse.resumeme.domain.mentee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.domain.Field;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.validate;

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

    @Enumerated(STRING)
    private Field field;

    public MenteeField(Field field, Mentee mentee) {
        validate(field == null, "NO_EMPTY_VALUE", "분야는 필수 값입니다");
        validate(mentee == null, "NO_EMPTY_VALUE", "사용자는 필수 값입니다");

        this.field = field;
        this.mentee = mentee;
    }

}
