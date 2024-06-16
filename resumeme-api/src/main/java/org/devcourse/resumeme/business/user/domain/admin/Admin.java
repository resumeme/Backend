package org.devcourse.resumeme.business.user.domain.admin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.common.util.Validator;

import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.Condition.isBlank;
import static org.devcourse.resumeme.common.util.Validator.check;
import static org.devcourse.resumeme.global.exception.ExceptionCode.NO_EMPTY_VALUE;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Admin {

    @Id
    @GeneratedValue
    @Column(name = "admin_id")
    private Long id;

    private String email;

    private String password;

    public Admin(String email, String password) {
        Validator.check(isBlank(email), NO_EMPTY_VALUE);
        Validator.check(isBlank(password), NO_EMPTY_VALUE);

        this.email = email;
        this.password = password;
    }

}
