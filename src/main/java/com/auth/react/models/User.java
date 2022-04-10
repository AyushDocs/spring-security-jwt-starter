package com.auth.react.models;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;

@Entity
@Builder
@Data
public class User {
      @Id
      private Long id;
      private String email;
      private String password;
}
