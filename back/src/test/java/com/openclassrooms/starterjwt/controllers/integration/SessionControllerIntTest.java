package com.openclassrooms.starterjwt.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.SessionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

    @MockBean
    private JwtUtils jwtUtils;

    @InjectMocks
    private SessionController sessionController;

    Session mockSession;

    SessionDto sessionDto;

    @BeforeEach
    public void setup() {
        Mockito.when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("token");
        Mockito.when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);

        Session mockSession = new Session();
        mockSession.setId(1L);
        mockSession.setName("Yoga");

        Mockito.when(sessionService.getById(1L)).thenReturn(mockSession);

        sessionDto = new SessionDto();
        sessionDto.setName("new session");
        sessionDto.setDescription("this is a new session");
        sessionDto.setDate(Date.from(Instant.now()));
        sessionDto.setTeacher_id(1L);
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindById() throws Exception {
        mockMvc.perform(get("/api/session/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/session/{id}", 0)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindByIdUnauthorized() throws Exception {
        mockMvc.perform(get("/api/session/{id}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/api/session")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindAllUnauthorized() throws Exception {
        mockMvc.perform(get("/api/session")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testCreateSession() throws Exception {        
        // Convert sessionDto to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String sessionDtoJson = objectMapper.writeValueAsString(sessionDto);
    
        // Perform the request
        mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sessionDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testDeleteSession() throws Exception {
        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testUpdateSession() throws Exception {  
        // Convert sessionDto to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String sessionDtoJson = objectMapper.writeValueAsString(sessionDto);

        // Perform the PUT request
        mockMvc.perform(put("/api/session/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sessionDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testParticipate() throws Exception {
        // Mocking session and user IDs
        Long sessionId = 1L;
        Long userId = 2L;

        // Mocking service response
        doNothing().when(sessionService).participate(sessionId, userId);

        // Perform the POST request
        mockMvc.perform(post("/api/session/1/participate/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify that the service method was called
        verify(sessionService).participate(sessionId, userId);
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testNoLongerParticipate() throws Exception {
        // Mocking session and user IDs
        Long sessionId = 1L;
        Long userId = 2L;

        // Mocking service response
        doNothing().when(sessionService).noLongerParticipate(sessionId, userId);

        // Perform the DELETE request
        mockMvc.perform(delete("/api/session/1/participate/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify that the service method was called
        verify(sessionService).noLongerParticipate(sessionId, userId);
    }
}
