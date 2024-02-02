package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    Teacher teacher1 = new Teacher(1L, "levesque1", "Arthur1", LocalDateTime.now(), LocalDateTime.now());
    Teacher teacher2 = new Teacher(2L, "levesque2", "Arthur2", LocalDateTime.now(), LocalDateTime.now());

    private List<Teacher> teacherList;


    @BeforeEach
    void setUp() {
        teacherList = List.of(teacher1, teacher2);
    }

    @Test
    void testFindAll() {
        when(teacherRepository.findAll()).thenReturn(teacherList);

        List<Teacher> result = teacherService.findAll();

        assertEquals(teacherList, result);
    }

    @Test
    void testFindById() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacherList.get(0)));

        Teacher result = teacherService.findById(1L);

        assertEquals(List.of(teacher1), result);
        assertNotNull(result);
        assertEquals("levesque1", result.getLastName());
        assertEquals("Arthur1", result.getFirstName());
    }
}
