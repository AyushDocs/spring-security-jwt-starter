package com.auth.react.services;

import java.util.Optional;

import com.auth.react.dto.LoginRequest;
import com.auth.react.dto.SignupRequest;
import com.auth.react.exceptions.InvalidCredentialsException;
import com.auth.react.exceptions.NoSuchUserException;
import com.auth.react.exceptions.UserAlreadyExistsException;
import com.auth.react.models.User;
import com.auth.react.repository.UserRepository;
import com.auth.react.utils.JwtUtils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserService {

      private final UserRepository userRepository;
      private final PasswordEncoder passwordEncoder;
      private final JwtUtils jwtUtils; 

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

    public String login(LoginRequest loginRequest) {
        String email=loginRequest.getEmail();
        String password=loginRequest.getPassword();
        User user = userRepository.findByEmail(email).orElseThrow(NoSuchUserException::new);
        if(!passwordEncoder.matches(password, user.getPassword()))throw new InvalidCredentialsException();
        return jwtUtils.generateToken(user);
    }

}
