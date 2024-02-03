package com.openclassrooms.starterjwt.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.controllers.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthController authController;

    private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    private LoginRequest loginRequest;
    private LoginRequest loginRequestInvalid;

    private SignupRequest signupRequest;

    @BeforeEach
    public void setup() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        loginRequestInvalid = new LoginRequest();
        loginRequestInvalid.setEmail("invalid@studio.com");
        loginRequestInvalid.setPassword("invalid!1234");

        signupRequest = new SignupRequest();
        signupRequest.setEmail("test@email.com");
        signupRequest.setFirstName("hello");
        signupRequest.setLastName("world");
        signupRequest.setPassword("password123");
    }

    @Test
    public void testAuthenticateAdmin() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testAuthenticateUser() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterUserOk() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ow.writeValueAsString(signupRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterUserEmailAlreadyTaken() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
