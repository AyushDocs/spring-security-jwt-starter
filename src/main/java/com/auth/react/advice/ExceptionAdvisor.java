package com.auth.react.advice;

import com.auth.react.exceptions.UserAlreadyExistsException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvisor {
      @ExceptionHandler(UserAlreadyExistsException.class)
      @ResponseStatus(HttpStatus.BAD_REQUEST)
      public void UserAlreadyExistsException(UserAlreadyExistsException ex){

      }
}
