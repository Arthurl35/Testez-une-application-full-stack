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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @Test
    void testCreate() {
        Session expectedSession = new Session();
        expectedSession.setId(1L);

        when(this.sessionRepository.save(eq(expectedSession))).thenReturn(expectedSession);

        Session actualSession = this.sessionService.create(expectedSession);

        verify(this.sessionRepository).save(eq(expectedSession));

        assertEquals(expectedSession, actualSession);
    }

    @Test
    void testDelete() {
        sessionService.delete(1L);
        verify(this.sessionRepository).deleteById(eq(1L));
    }

    @Test
    void testFindAll() {
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

    List<Session> actualSessions = sessionService.findAll();

    verify(sessionRepository).findAll();

    assertEquals(expectedSessions, actualSessions);
    }

    @Test
    void testGetById() {
    Session expectedSession = Session.builder()
        .id(1L)
        .build();

    when(sessionRepository.findById(eq(1L))).thenReturn(Optional.of(expectedSession));

    Session actualSession = sessionService.getById(1L);

    verify(sessionRepository).findById(eq(1L));

    assertEquals(expectedSession, actualSession);
    }

    @Test
    void testNoLongerParticipate() {
        User user = new User();
        user.setId(1L);
        Session expectedSession = new Session();
        expectedSession.setId(1L);
        expectedSession.setUsers(Arrays.asList(user));

        when(sessionRepository.findById(eq(1L))).thenReturn(Optional.of(expectedSession));

        sessionService.noLongerParticipate(1L, 1L);

        verify(sessionRepository).save(eq(expectedSession));
    }

    @Test
    void testNoLongerParticipate_SessionNotFound() {
        when(sessionRepository.findById(eq(1L))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    @Test
    void testNoLongerParticipate_UserNotParticipating() {
        Session expectedSession = new Session();
        expectedSession.setId(1L);
        expectedSession.setUsers(new ArrayList<>());

        when(sessionRepository.findById(eq(1L))).thenReturn(Optional.of(expectedSession));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    @Test
    void testParticipate() {
        User user = new User();
        user.setId(1L);
        Session expectedSession = new Session();
        expectedSession.setId(1L);
        expectedSession.setUsers(new ArrayList<>());

        when(sessionRepository.findById(eq(1L))).thenReturn(Optional.of(expectedSession));
        when(userRepository.findById(eq(1L))).thenReturn(Optional.of(user));

        sessionService.participate(1L, 1L);

        verify(sessionRepository).save(eq(expectedSession));

        assertTrue(expectedSession.getUsers().contains(user));
    }

    @Test
    void testParticipate_SessionNotFound() {
        when(sessionRepository.findById(eq(1L))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void testParticipate_UserNotFound() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(new Session()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }
        
    @Test
    void testUpdate() {
        Session expectedSession = new Session();
        expectedSession.setId(1L);

        when(sessionRepository.save(eq(expectedSession))).thenReturn(expectedSession);

        Session actualSession = sessionService.update(1L, expectedSession);

        verify(sessionRepository).save(eq(expectedSession));

        assertEquals(expectedSession, actualSession);
    }
}
