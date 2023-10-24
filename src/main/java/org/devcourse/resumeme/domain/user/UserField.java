package org.devcourse.resumeme.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static org.devcourse.resumeme.common.util.Validator.validate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserField {

    @Id
    @GeneratedValue
    @Column(name = "user_field_id")
    private Long id;

    @Enumerated(STRING)
    private Field field;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public UserField(Field field, User user) {
        validate(field == null, "NO_EMPTY_VALUE", "분야는 필수 값입니다");
        validate(user == null, "NO_EMPTY_VALUE", "사용자는 필수 값입니다");

        this.field = field;
        this.user = user;
    }

}
