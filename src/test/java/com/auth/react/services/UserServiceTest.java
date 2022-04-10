package com.auth.react.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.auth.react.dto.LoginRequest;
import com.auth.react.dto.SignupRequest;
import com.auth.react.exceptions.InvalidCredentialsException;
import com.auth.react.exceptions.NoSuchUserException;
import com.auth.react.exceptions.UserAlreadyExistsException;
import com.auth.react.models.User;
import com.auth.react.repository.UserRepository;
import com.auth.react.utils.JwtUtils;

import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
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
      private LoginRequest loginRequest;
      @Mock
      private UserRepository repository;
      @Mock
      private PasswordEncoder encoder;
      @Mock
      private JwtUtils jwtUtils;

      @BeforeEach
      void setUp() {
            underTest = new UserService(repository, encoder, jwtUtils);
            signupRequest = SignupRequest.builder()
                        .email("ayush@gmail.com")
                        .password("secret")
                        .build();
            loginRequest = LoginRequest.builder()
                        .email("ayush@gmail.com")
                        .password("secret")
                        .build();
      }

      @Test
      void signup_should_not_succeed_user_already_exists() throws Exception {
            when(repository.existsByEmail("ayush@gmail.com")).thenReturn(true);
            assertThrowsExactly(UserAlreadyExistsException.class,
                        () -> underTest.signup(signupRequest));
            verify(repository).existsByEmail("ayush@gmail.com");

      }

      @Test
      void signup_should_succeed() throws Exception {
            when(repository.existsByEmail("ayush@gmail.com")).thenReturn(false);
            when(encoder.encode("secret")).thenReturn("encodedPassword");
            assertDoesNotThrow(() -> underTest.signup(signupRequest));
            verify(repository).existsByEmail("ayush@gmail.com");
            verify(encoder).encode("secret");
            ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
            verify(repository).save(ac.capture());
            User savedUser = ac.getValue();
            assertNotNull(savedUser);
            assertEquals("ayush@gmail.com", savedUser.getEmail());
            assertEquals("encodedPassword", savedUser.getPassword());
      }

      @Test
      void login_should_not_succeed_user_does_not_already_exist() throws Exception {
            when(repository.findByEmail("ayush@gmail.com")).thenReturn(Optional.ofNullable(null));
            assertThrowsExactly(NoSuchUserException.class,
                        () -> underTest.login(loginRequest));
            verify(repository).findByEmail("ayush@gmail.com");

      }

      @Test
      void login_should_not_succeed_invalid_password() throws Exception {
            User user = User.builder()
                        .email("ayush@gmail.com")
                        .password("secret")
                        .build();
            when(repository.findByEmail("ayush@gmail.com")).thenReturn(Optional.of(user));
            when(encoder.matches("secret", "secret")).thenReturn(false);
            assertThrowsExactly(InvalidCredentialsException.class, () -> underTest.login(loginRequest));
            verify(repository).findByEmail("ayush@gmail.com");
            verify(encoder).matches("secret", "secret");
      }

      @Test
      void login_should_succeed() throws Exception {
            User user = User.builder()
                        .email("ayush@gmail.com")
                        .password("secret")
                        .build();
            when(repository.findByEmail("ayush@gmail.com")).thenReturn(Optional.of(user));
            when(encoder.matches("secret", "secret")).thenReturn(true);
            when(jwtUtils.generateToken(user)).thenReturn("token");
            assertEquals("token", underTest.login(loginRequest));
            verify(repository).findByEmail("ayush@gmail.com");
            verify(encoder).matches("secret", "secret");
            verify(jwtUtils).generateToken(user);

      }
}
