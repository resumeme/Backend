package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOauthUsername(String oauthUsername);

}
