package com.openclassrooms.starterjwt.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest;
    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("toto3@toto.com");
        loginRequest.setPassword("test!1234");

        signupRequest = new SignupRequest();
        signupRequest.setEmail("ddddddssddddddsd@example.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("Jaohn");
        signupRequest.setLastName("Doe");
    }

    @Test
    void authenticateUser() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword());
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username(loginRequest.getEmail())
                .firstName("toto")
                .lastName("toto")
                .admin(false)
                .password("password")
                .build();
        String jwt = "jwtToken";

        when(authenticationManager.authenticate(authentication)).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(jwt);
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(loginRequest.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(userDetails.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(userDetails.getLastName()));
    }

    @Test
    void registerUser() throws Exception {
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User registered successfully!"));
    }

    // Méthode utilitaire pour convertir un objet en chaîne JSON
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
