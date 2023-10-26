package org.devcourse.resumeme.global.auth.model;

import org.devcourse.resumeme.domain.user.UserCommonInfo;
import java.util.Date;

public record Claims(Long id, String role, Date expiration) {

    public static Claims of(UserCommonInfo info) {
        return new Claims(info.id(), info.role().toString(), new Date());
    }

}
