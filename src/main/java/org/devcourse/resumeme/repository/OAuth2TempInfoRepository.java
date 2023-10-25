package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.global.auth.model.OAuth2TempInfo;
import org.springframework.data.repository.CrudRepository;

public interface OAuth2TempInfoRepository extends CrudRepository<OAuth2TempInfo, String> {

}
