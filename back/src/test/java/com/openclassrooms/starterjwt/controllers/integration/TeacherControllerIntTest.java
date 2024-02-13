package com.openclassrooms.starterjwt.controllers.integration;

import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.TeacherService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.core.Authentication;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIntTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private TeacherRepository teacherRepository;

    @MockBean
    private TeacherMapper teacherMapper;

    @MockBean
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setup() {
        Teacher mockTeacher = new Teacher();
        mockTeacher.setId(1L);
        
        Mockito.when(teacherService.findById(1L)).thenReturn(mockTeacher);
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindById() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindByIdUnauthorized() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", 1L))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindByIdBadRequest() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", "abc"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", 0))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindAll() throws Exception {
        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindAllUnauthorized() throws Exception {
        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isUnauthorized());
    }


}