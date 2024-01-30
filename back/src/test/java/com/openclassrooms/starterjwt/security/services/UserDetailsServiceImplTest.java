package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User expectedUser;

    @BeforeEach
    void init() {
        expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setEmail("Arthur@LEVESQUE.com");
        expectedUser.setFirstName("Arthur");
        expectedUser.setLastName("LEVESQUE");
        expectedUser.setPassword("password"); 
    }

    @Test
    void testLoadUserByUsername() {
        when(userRepository.findByEmail(eq("Arthur@LEVESQUE.com"))).thenReturn(Optional.of(expectedUser));

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername("Arthur@LEVESQUE.com");

        verify(userRepository).findByEmail(eq("Arthur@LEVESQUE.com"));

        assertEquals(expectedUser.getId(), userDetails.getId());
        assertEquals(expectedUser.getEmail(), userDetails.getUsername());
        assertEquals(expectedUser.getLastName(), userDetails.getLastName());
        assertEquals(expectedUser.getFirstName(), userDetails.getFirstName());
        assertEquals(expectedUser.getPassword(), userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail(eq("Arthur@LEVESQUE.com"))).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("Arthur@LEVESQUE.com"));

        verify(userRepository).findByEmail(eq("Arthur@LEVESQUE.com"));
    }
}