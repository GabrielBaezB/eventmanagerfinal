package com.eventmanager.service;

import com.eventmanager.model.Event;
import com.eventmanager.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // Listar todos los eventos
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Crear un evento
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    // Obtener un evento por ID
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    // Eliminar un evento
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
