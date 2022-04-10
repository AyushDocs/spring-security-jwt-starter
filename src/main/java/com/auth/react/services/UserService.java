package com.auth.react.services;

import com.auth.react.dto.SignupRequest;
import com.auth.react.exceptions.UserAlreadyExistsException;
import com.auth.react.models.User;
import com.auth.react.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserService {

      private final UserRepository userRepository;
      private final PasswordEncoder passwordEncoder; 

      public void signup(SignupRequest signupRequest) {
          String email = signupRequest.getEmail();
          String password = signupRequest.getPassword();
          if(userRepository.existsByEmail(email)) throw new UserAlreadyExistsException();
          User user=User.builder()
                  .email(email)
                  .password(passwordEncoder.encode(password))
                  .build();
          userRepository.save(user);
      }

}
