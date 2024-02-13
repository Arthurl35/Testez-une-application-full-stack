package com.openclassrooms.starterjwt.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtUtils jwtUtils = new JwtUtils();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecret");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 600000);
    }

    @Test
    void testGenerateJwtToken() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "arthur@levesque.com", "arthur", "levesque", false, "1234");
        when(authentication.getPrincipal()).thenReturn(userDetails);
    
        String token = jwtUtils.generateJwtToken(authentication);
    
        assertNotNull(token);
    
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void testGetUserNameFromJwtToken() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "arthur@levesque.com", "arthur", "levesque", false, "1234");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);

        String userName = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals("arthur@levesque.com", userName);
    }

    @Test
    void testValidateJwtTokenWithInvalidToken() {
        assertFalse(jwtUtils.validateJwtToken("invalidToken"));
    }
}
