package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testDelete() {
        userService.delete(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void testFindById() {
        User expectedUser = new User();
        expectedUser.setId(1L);
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.findById(1L);

        verify(userRepository).findById(1L);

        assertEquals(expectedUser, actualUser, "The user returned by the service should match the expected user");
    }
}