package com.auth.react.repository;

import java.util.Optional;

import com.auth.react.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

      boolean existsByEmail(String string);

      Optional<User> findByEmail(String string);

}
