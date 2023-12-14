package org.devcourse.resumeme.global.auth.repository;

import org.devcourse.resumeme.global.auth.model.login.OAuth2TempInfo;
import org.springframework.data.repository.CrudRepository;

public interface OAuth2InfoRedisRepository extends CrudRepository<OAuth2TempInfo, String> {

}
