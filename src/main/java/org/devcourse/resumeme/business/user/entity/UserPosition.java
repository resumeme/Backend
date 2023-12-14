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
import org.devcourse.resumeme.common.domain.Position;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.devcourse.resumeme.common.util.Validator.check;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class UserPosition {

    @Id
    @GeneratedValue
    @Column(name = "user_position_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Enumerated(STRING)
    private Position position;

    public UserPosition(User user, Position position) {
        check(position == null, "NO_EMPTY_VALUE", "포지션은 필수 값입니다");
        check(user == null, "NO_EMPTY_VALUE", "사용자는 필수 값입니다");

        this.position = position;
        this.user = user;
    }

}
