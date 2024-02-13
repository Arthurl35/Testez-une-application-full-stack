
package com.openclassrooms.starterjwt.controllers.integration;

import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setup() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("yoga@studio.com");

        Mockito.when(userService.findById(1L)).thenReturn(mockUser);
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindById() throws Exception {
        mockMvc.perform(get("/api/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/user/{id}", 0))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindByIdBadRequest() throws Exception {
        mockMvc.perform(get("/api/user/{id}", "abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFindByIdUnauthorized() throws Exception {
        mockMvc.perform(get("/api/user/{id}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", 0))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testDeleteBadRequest() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", "abc"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testDeleteUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/user/{id}", 1L))
                .andExpect(status().isUnauthorized());
    }
}
