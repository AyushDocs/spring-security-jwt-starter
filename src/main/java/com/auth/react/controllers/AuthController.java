package com.auth.react.controllers;

import javax.validation.Valid;

import com.auth.react.dto.SignupRequest;
import com.auth.react.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class AuthController {
      private final UserService userService;
      @PostMapping("signup")
      @ResponseStatus(HttpStatus.CREATED)
      public void signup(@RequestBody @Valid SignupRequest signupRequest){
            userService.signup(signupRequest);
      }
}
