package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    //Initailisation des mocks
    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session expectedSession;

    private User expectedUser;

    @BeforeEach
    void init() {
        expectedSession = new Session();
        expectedSession.setId(1L);

        expectedUser = new User();
        expectedUser.setId(1L);
    }

    @Test
    void testCreate() {
        // Arrange (Configuration des mocks)
        when(this.sessionRepository.save(expectedSession)).thenReturn(expectedSession);

        // Act (Exécution de la méthode à tester)
        Session actualSession = this.sessionService.create(expectedSession);

        // Assert (Vérification des résultats)
        verify(this.sessionRepository).save(expectedSession);
        assertEquals(expectedSession, actualSession);
    }

    @Test
    void testDelete() {
        // Arrange & Act
        sessionService.delete(1L);

        // Assert
        verify(this.sessionRepository).deleteById(eq(1L));
    }

    @Test
    void testFindAll() {
        // Arrange
        List<Session> expectedSessions = new ArrayList<>();
        expectedSessions.add(Session.builder()
            .id(1L)
            .name("Session 1")
            .build());
        expectedSessions.add(Session.builder()
            .id(2L)
            .name("Session 2")
            .build());

        when(sessionRepository.findAll()).thenReturn(expectedSessions);

        // Act
        List<Session> actualSessions = sessionService.findAll();

        // Assert
        verify(sessionRepository).findAll();
        assertEquals(expectedSessions, actualSessions);
    }

    @Test
    void testGetById() {
        // Arrange
        when(sessionRepository.findById(eq(1L))).thenReturn(Optional.of(expectedSession));

        // Act
        Session actualSession = sessionService.getById(1L);

        // Assert
        verify(sessionRepository).findById(eq(1L));
        assertEquals(expectedSession, actualSession);
    }

    @Test
    void testNoLongerParticipate() {
        // Arrange
        expectedSession.setUsers(Arrays.asList(expectedUser));

        when(sessionRepository.findById(eq(1L))).thenReturn(Optional.of(expectedSession));

        // Act
        sessionService.noLongerParticipate(1L, 1L);

        // Assert
        verify(sessionRepository).save(eq(expectedSession));
    }

    @Test
    void testNoLongerParticipateSessionNotFound() {
        // Arrange
        when(sessionRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    @Test
    void testNoLongerParticipateUserNotParticipating() {
        // Arrange
        expectedSession.setUsers(new ArrayList<>());

        when(sessionRepository.findById(eq(1L))).thenReturn(Optional.of(expectedSession));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    @Test
    void testParticipate() {
        // Arrange
        expectedSession.setUsers(new ArrayList<>());

        when(sessionRepository.findById(eq(1L))).thenReturn(Optional.of(expectedSession));
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(expectedUser));

        // Act
        sessionService.participate(1L, 1L);

        // Assert
        verify(sessionRepository).save(eq(expectedSession));
        assertTrue(expectedSession.getUsers().contains(expectedUser));
    }

    @Test
    void testParticipateSessionNotFound() {
        // Arrange
        when(sessionRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void testParticipateUserNotFound() {
        // Arrange
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(new Session()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void testParticipateAlreadyParticipating() {
        // Arrange
        Session expectedSession = new Session();
        expectedSession.setId(1L);
        expectedSession.setUsers(new ArrayList<>());
        expectedSession.getUsers().add(expectedUser);

        when(sessionRepository.findById(eq(1L))).thenReturn(Optional.of(expectedSession));
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(expectedUser));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));

        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void testUpdate() {
        // Arrange
        when(sessionRepository.save(eq(expectedSession))).thenReturn(expectedSession);

        // Act
        Session actualSession = sessionService.update(1L, expectedSession);

        // Assert
        verify(sessionRepository).save(eq(expectedSession));
        assertEquals(expectedSession, actualSession);
    }
}
