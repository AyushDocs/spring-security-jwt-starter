package com.auth.react.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class SignupRequest {
      @Email
      @NotNull
      @NotBlank
      @NotEmpty
      private String email;
      @NotNull
      @NotBlank
      @NotEmpty
      private String password;
}
