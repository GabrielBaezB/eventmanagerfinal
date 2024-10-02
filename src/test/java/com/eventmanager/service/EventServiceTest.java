package com.eventmanager.service;

import com.eventmanager.model.Event;
import com.eventmanager.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEvents() {
        // Dado un conjunto de eventos
        List<Event> events = Arrays.asList(
                new Event("Evento 1", "Descripción 1", "Ubicación 1", LocalDate.now()),
                new Event("Evento 2", "Descripción 2", "Ubicación 2", LocalDate.now())
        );
        
        // Cuando el repositorio los devuelve
        when(eventRepository.findAll()).thenReturn(events);

        // Entonces el servicio debe devolverlos también
        List<Event> result = eventService.getAllEvents();
        assertEquals(2, result.size());
        assertEquals("Evento 1", result.get(0).getName());
    }

    @Test
    void testSaveEvent() {
        // Dado un evento
        Event event = new Event("Evento Test", "Descripción Test", "Ubicación Test", LocalDate.now());
        
        // Cuando guardamos el evento
        when(eventRepository.save(event)).thenReturn(event);

        // Entonces el servicio debe devolver el evento guardado
        Event result = eventService.saveEvent(event);
        assertEquals("Evento Test", result.getName());
    }

    @Test
    void testGetEventById() {
        // Dado un evento con un ID específico
        Event event = new Event("Evento Test", "Descripción Test", "Ubicación Test", LocalDate.now());
        event.setId(1L);

        // Cuando el repositorio lo devuelve por ID
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        // Entonces el servicio debe devolver el evento encontrado
        Optional<Event> result = eventService.getEventById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testDeleteEvent() {
        // Dado un evento con ID 1
        Long eventId = 1L;

        // Cuando eliminamos el evento
        doNothing().when(eventRepository).deleteById(eventId);
        
        eventService.deleteEvent(eventId);

        // Entonces el método deleteById debe ser invocado una vez
        verify(eventRepository, times(1)).deleteById(eventId);
    }
}
