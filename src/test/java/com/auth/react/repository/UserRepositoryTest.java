package com.auth.react.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.auth.react.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class UserRepositoryTest {
      @Autowired
      private UserRepository userRepository;
      @Autowired
      private TestEntityManager em;
      @BeforeEach
      void setup(){
            em.clear();
      }
      @Test
      void user_should_exist_by_email() {
            User user = User.builder()
                        .email("ayush@g.com")
                        .password("password")
                        .id(1l)
                        .build();
            em.persistAndFlush(user);
            assertTrue(userRepository.existsByEmail("ayush@g.com"));
      }
      
      @Test
      void user_should_not_exist_by_email() {
            assertFalse(userRepository.existsByEmail("ayush@g.com"));
      }
}
