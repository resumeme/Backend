package org.devcourse.resumeme.repository;

import org.devcourse.resumeme.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
