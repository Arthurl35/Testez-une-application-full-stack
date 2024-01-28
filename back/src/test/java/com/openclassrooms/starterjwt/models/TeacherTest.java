package com.openclassrooms.starterjwt.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import javax.validation.ConstraintViolationException;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.openclassrooms.starterjwt.repository.TeacherRepository;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

public class TeacherTest {
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Test
    void testConstructorWithValues() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("LEVESQUE");
        teacher.setFirstName("Arthur");
        
        LocalDateTime fixedDateTime = LocalDateTime.of(2022, 1, 1, 0, 0);
        teacher.setCreatedAt(fixedDateTime);
        teacher.setUpdatedAt(fixedDateTime);

        assertNotNull(teacher);

        assertEquals(1L, teacher.getId());
        assertEquals("LEVESQUE", teacher.getLastName());
        assertEquals("Arthur", teacher.getFirstName());
        assertEquals(fixedDateTime, teacher.getCreatedAt());
        assertEquals(fixedDateTime, teacher.getUpdatedAt());
    }
    
    

}
