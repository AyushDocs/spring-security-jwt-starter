package com.auth.react.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.auth.react.dto.SignupRequest;
import com.auth.react.exceptions.UserAlreadyExistsException;
import com.auth.react.models.User;
import com.auth.react.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class UserServiceTest {
      private UserService underTest;
      private SignupRequest signupRequest;
      @Mock
      private UserRepository repository;
      @Mock
      private PasswordEncoder encoder;
      @BeforeEach
      void setUp(){
            underTest = new UserService(repository,encoder);
            signupRequest = SignupRequest.builder()
                        .email("ayush@gmail.com")
                        .password("secret")
                        .build();
      }

      @Test
      void signup_should_not_succeed_user_already_exists() throws Exception {
           when(repository.existsByEmail("ayush@gmail.com")).thenReturn(true);
           assertThrowsExactly(UserAlreadyExistsException.class,
            ()->underTest.signup(signupRequest));
            verify(repository).existsByEmail("ayush@gmail.com");
           
      }

      @Test
      void signup_should_succeed() throws Exception {
            when(repository.existsByEmail("ayush@gmail.com")).thenReturn(false);
            when(encoder.encode("secret")).thenReturn("encodedPassword");
            assertDoesNotThrow(() -> underTest.signup(signupRequest));
            verify(repository).existsByEmail("ayush@gmail.com");
            verify(encoder).encode("secret");
            ArgumentCaptor<User> ac=ArgumentCaptor.forClass(User.class);
            verify(repository).save(ac.capture());
            User savedUser=ac.getValue();
            assertNotNull(savedUser);
            assertEquals("ayush@gmail.com", savedUser.getEmail());
            assertEquals("encodedPassword", savedUser.getPassword());
      }
}
