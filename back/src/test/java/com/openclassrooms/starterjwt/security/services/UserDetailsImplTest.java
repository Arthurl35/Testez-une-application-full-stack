package com.openclassrooms.starterjwt.security.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserDetailsImplTest {

    private UserDetailsImpl user1;
    private UserDetailsImpl user2;
    private UserDetailsImpl user3;

    @BeforeEach
    void setUp() {
        user1 = UserDetailsImpl.builder().id(1L).build();
        user2 = UserDetailsImpl.builder().id(1L).build();
        user3 = UserDetailsImpl.builder().id(2L).build();
    }

    @Test
    void testBooleanMethods() {
        assertTrue(user1.isAccountNonExpired());
        assertTrue(user1.isAccountNonLocked());
        assertTrue(user1.isCredentialsNonExpired());
        assertTrue(user1.isEnabled());
    }

    @Test
    void testEquals() {
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }
}