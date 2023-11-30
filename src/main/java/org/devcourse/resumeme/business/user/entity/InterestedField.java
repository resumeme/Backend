package org.devcourse.resumeme.business.user.entity;

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
import static org.devcourse.resumeme.common.util.Validator.notNull;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class InterestedField {

    @Id
    @GeneratedValue
    @Column(name = "interested_field_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Enumerated(STRING)
    private Field field;

    public InterestedField(User user, Field field) {
        notNull(user);
        notNull(field);

        this.user = user;
        this.field = field;
    }
}
