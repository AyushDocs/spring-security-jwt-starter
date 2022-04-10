package com.auth.react.controllers;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.auth.react.dto.SignupRequest;
import com.auth.react.exceptions.UserAlreadyExistsException;
import com.auth.react.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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

      @BeforeEach
      void setUp() {
            signupRequest = SignupRequest.builder()
                        .email("ayysh@gmail.com")
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

      private String asJsonString(Object obj) throws JsonProcessingException {
            return mapper.writeValueAsString(obj);
      }

}
