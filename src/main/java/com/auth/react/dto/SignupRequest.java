package com.auth.react.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class SignupRequest {
      @Email
      @NotNull
      private String email;
      @NotNull
      private String password;
}
