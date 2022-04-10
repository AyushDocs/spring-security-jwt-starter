package com.auth.react.controllers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.auth.react.dto.LoginRequest;
import com.auth.react.dto.SignupRequest;
import com.auth.react.exceptions.NoSuchUserException;
import com.auth.react.exceptions.UserAlreadyExistsException;
import com.auth.react.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest
@AutoConfigureMockMvc
class AuthControllerTest {
      @Autowired
      private MockMvc mvc;
      private static final ObjectMapper mapper = new ObjectMapper();
      private SignupRequest signupRequest;
      @MockBean
      private UserService userService;
      private LoginRequest loginRequest;

      @BeforeEach
      void setUp() {
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
      void signup_should_fail_no_body() throws Exception {

            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/signup");
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void signup_should_fail_content_type_not_set() throws Exception {
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/signup")
                        .content(asJsonString(signupRequest));
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType());
      }

      @Test
      void signup_should_fail_improper_email() throws Exception {
            signupRequest.setEmail("ayushgmail.com");
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/signup")
                        .content(asJsonString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void signup_should_fail_empty_email() throws Exception {
            signupRequest.setEmail("");
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/signup")
                        .content(asJsonString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void signup_should_fail_email_blank_() throws Exception {
            signupRequest.setEmail(" ");
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/signup")
                        .content(asJsonString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void signup_should_fail_empty_password() throws Exception {
            signupRequest.setEmail("");
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/signup")
                        .content(asJsonString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void signup_should_fail_password_blank() throws Exception {
            signupRequest.setPassword(" ");
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/signup")
                        .content(asJsonString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void signup_should_fail_password_not_provided() throws Exception {
            signupRequest.setPassword(null);
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/signup")
                        .content(asJsonString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void signup_should_fail_email_not_provided() throws Exception {
            signupRequest.setEmail(null);
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/signup")
                        .content(asJsonString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void signup_should_fail_email_and_password_not_provided() throws Exception {
            signupRequest.setPassword(null);
            signupRequest.setEmail(null);
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/signup")
                        .content(asJsonString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void signup_should_not_succeed_user_already_exists() throws Exception {
            doThrow(new UserAlreadyExistsException())
                        .when(userService).signup(signupRequest);
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/signup")
                        .content(asJsonString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void signup_should_succeed() throws Exception {
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/signup")
                        .content(asJsonString(signupRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isCreated());
            verify(userService).signup(signupRequest);
      }

      // ----------------------------------------------------------------------------------------
      @Test
      void login_should_fail_no_body() throws Exception {
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/login");
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void login_should_fail_no_content_type_set() throws Exception {
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/login")
                        .content(asJsonString(loginRequest));
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType());
      }

      @Test
      void login_should_fail_improper_email() throws Exception {
            loginRequest.setEmail("ayushgmail.com");
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/login")
                        .content(asJsonString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void login_should_fail_no_password() throws Exception {
            loginRequest.setPassword(null);
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/login")
                        .content(asJsonString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void login_should_fail_no_email() throws Exception {
            loginRequest.setEmail(null);
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/login")
                        .content(asJsonString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void login_should_fail_no_email_and_password() throws Exception {
            loginRequest.setEmail(null);
            loginRequest.setPassword(null);
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/login")
                        .content(asJsonString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void login_should_fail_empty_email() throws Exception {
            loginRequest.setEmail("");
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/login")
                        .content(asJsonString(
                                    loginRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void login_should_fail_email_blank_() throws Exception {
            loginRequest.setEmail(" ");
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/login")
                        .content(asJsonString(
                                    loginRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void login_should_fail_empty_password() throws Exception {
            loginRequest.setEmail("");
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/login")
                        .content(asJsonString(
                                    loginRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void login_should_fail_password_blank() throws Exception {
            loginRequest.setPassword(" ");
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/login")
                        .content(asJsonString(
                                    loginRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
      }

      @Test
      void login_should_not_succeed_user_does_not_exist() throws Exception {
            when(userService.login(loginRequest)).thenThrow(new NoSuchUserException());
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/login")
                        .content(asJsonString(
                                    loginRequest))
                        .contentType(MediaType.APPLICATION_JSON);
            mvc.perform(request)
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
            verify(userService).login(loginRequest);
      }

      @Test
      void login_should_succeed() throws Exception {
            when(userService.login(loginRequest)).thenReturn("jwt");
            MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                        .post("/api/v1/users/login")
                        .content(asJsonString(
                                    loginRequest))
                        .contentType(MediaType.APPLICATION_JSON);

            ResultActions actions = mvc.perform(request);
            verify(userService).login(loginRequest);
            actions
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.cookie().exists("token"))
                        .andExpect(MockMvcResultMatchers.cookie().httpOnly("token", true))
                        .andExpect(MockMvcResultMatchers.cookie().value("token", "jwt"))
                        .andExpect(MockMvcResultMatchers.cookie().secure("token", true));
      }

      private String asJsonString(Object obj) throws JsonProcessingException {
            return mapper.writeValueAsString(obj);
      }

}
